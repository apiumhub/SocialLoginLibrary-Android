package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookLoginManager(
        private val facebookConfiguration: FacebookConfiguration,
        override val fragment: Fragment? = null,
        override val activity: Activity? = null) : SocialManager {

    private var callbackManager: CallbackManager? = null

    init {
        if (fragment == null && activity == null) {
            throw IllegalArgumentException("Either a fragment or an activity must be provided on the constructor")
        }
        else if (fragment != null && activity != null) {
            throw IllegalArgumentException("Provide only a fragment OR an activity")
        }
    }

    override fun login() {
        callbackManager = CallbackManager.Factory.create()
        when {
            fragment != null -> LoginManager.getInstance().logInWithReadPermissions(fragment, facebookConfiguration.permissions)
            activity != null -> LoginManager.getInstance().logInWithReadPermissions(activity, facebookConfiguration.permissions)
            else -> throw IllegalStateException("Fragment and activity are both null")
        }
    }

    override fun logout() {
        LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        requestEmail(success, error)
                    }
                    override fun onCancel() {
                        error(SocialLoginException(SocialLoginErrorType.CANCELED))
                    }
                    override fun onError(error: FacebookException?) {
                        error(SocialLoginException(SocialLoginErrorType.FAILED))
                    }
                })
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }

    private fun requestEmail(success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {
        GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), {json, response ->
            if (response.error != null || !json.has("email")) {
                error(SocialLoginException(SocialLoginErrorType.UNKNOWN, response.error.exception))
            }
            else {
                success(SocialUserInformation(AccessToken.getCurrentAccessToken().userId, AccessToken.getCurrentAccessToken().token, json.getString("email")))
            }
        }).apply {
            val parameters = Bundle().apply {
                putString("fields", "email")
            }
            setParameters(parameters)
            executeAsync()
        }
    }
}