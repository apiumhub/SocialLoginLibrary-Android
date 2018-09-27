package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.apiumhub.social.library.SocialLoginErrorType.CANCELED
import com.apiumhub.social.library.SocialLoginErrorType.FAILED
import com.apiumhub.social.library.SocialLoginErrorType.PERMISSIONS
import com.linkedin.platform.APIHelper
import com.linkedin.platform.LISessionManager
import com.linkedin.platform.errors.LIApiError
import com.linkedin.platform.errors.LIAppErrorCode.USER_CANCELLED
import com.linkedin.platform.errors.LIAuthError
import com.linkedin.platform.listeners.ApiListener
import com.linkedin.platform.listeners.ApiResponse
import com.linkedin.platform.listeners.AuthListener
import com.linkedin.platform.utils.Scope
import com.linkedin.platform.utils.Scope.LIPermission

class LinkedinLoginManager
private constructor(
    configuration: LinkedinConfiguration,
    override val activity: Activity,
    override val fragment: androidx.fragment.app.Fragment? = null) : SocialManager {
    private val linkedinManager: LISessionManager

    init {
        if (activity.applicationContext == null) {
            throw IllegalArgumentException("Activity application context should not be null")
        } else {
            linkedinManager = LISessionManager.getInstance(activity)
        }
    }


    private val scope = Scope.build(*configuration.scopes.toTypedArray())
    private val linkedinInfoTag: String = "https://api.linkedin.com/v1/people/~:(${configuration.permissions.joinToString(
        ",")})?format=json"

    private val id = "id"
    private val email = "emailAddress"
    private lateinit var error: (error: SocialLoginException) -> Unit
    private lateinit var success: (user: SocialUserInformation) -> Unit

    override fun login() {
        linkedinManager.init(activity, scope, object : AuthListener {
            override fun onAuthSuccess() {
                getUserInfo()
            }

            override fun onAuthError(error: LIAuthError?) {
                errorHandler(error)
            }

        }, true)
    }

    override fun logout() {
        linkedinManager.clearSession()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit,
        error: (error: SocialLoginException) -> Unit) {
        this.success = success
        this.error = error
        linkedinManager.onActivityResult(activity, requestCode, resultCode, data)
    }


    private fun getUserInfo() {
        APIHelper.getInstance(activity.applicationContext).getRequest(activity, linkedinInfoTag,
            object : ApiListener {
                override fun onApiSuccess(apiResponse: ApiResponse?) {
                    successHandler(apiResponse)
                }

                override fun onApiError(LIApiError: LIApiError?) {
                    error(SocialLoginException(CANCELED, Throwable(LIApiError.toString())))
                }

            })
    }

    private fun successHandler(apiResponse: ApiResponse?) {
        val json = apiResponse?.responseDataAsJson
        if (json?.getString(email).isNullOrBlank() && json?.getString(id).isNullOrBlank()) {
            error(SocialLoginException(PERMISSIONS, Throwable("Check request scope permission")))

        } else {
            success(SocialUserInformation(json!!.getString(id), "", json.getString(email)))
        }
    }

    private fun errorHandler(error: LIAuthError?) {
        if (error.toString().contains(USER_CANCELLED.name)) {
            error(SocialLoginException(CANCELED, Throwable(error.toString())))
        } else {
            error(SocialLoginException(FAILED, Throwable(error.toString())))
        }
    }

    object LinkedinConfigurationBuilder {
        private val permissionsList: MutableList<String> = mutableListOf()
        private val scopes: MutableSet<LIPermission> = mutableSetOf()

        fun requestEmail(): LinkedinConfigurationBuilder {
            permissionsList.add("email-address")
            scopes.add(Scope.R_EMAILADDRESS)
            return this
        }

        fun requestId(): LinkedinConfigurationBuilder {
            permissionsList.add("id")
            scopes.add(Scope.R_BASICPROFILE)
            return this
        }

        fun requestProfilePicture(): LinkedinConfigurationBuilder {
            permissionsList.add("profile-picture")
            scopes.add(Scope.R_BASICPROFILE)
            return this
        }

        fun build(activity: Activity): LinkedinLoginManager {
            return LinkedinLoginManager(LinkedinConfiguration(permissionsList, scopes), activity)
        }
    }
}

