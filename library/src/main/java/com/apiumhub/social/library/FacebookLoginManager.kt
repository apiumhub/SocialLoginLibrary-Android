package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import com.facebook.login.LoginManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.facebook.FacebookException

data class FacebookConfiguration(val appId: String, val permissions: List<String>): SocialNetworkConfiguration
data class SocialLoginException(val loginError: SocialLoginErrorType): Throwable()

enum class SocialLoginErrorType {
    CANCELED, FAILED, PERMISSIONS
}

class FacebookLoginManager(private val facebookConfiguration: FacebookConfiguration) : SocialManager {


    private var callbackManager: CallbackManager? = null
    override fun login( activity: Activity, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {

        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object: FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        success(SocialUserInformation(loginResult.accessToken.userId,loginResult.accessToken.token))
                    }
                    override fun onCancel() {
                        error(SocialLoginException(SocialLoginErrorType.CANCELED))
                    }
                    override fun onError(error: FacebookException?) {
                        error(SocialLoginException(SocialLoginErrorType.FAILED))
                    }
                })
        LoginManager.getInstance().logInWithReadPermissions(activity, facebookConfiguration.permissions)
    }

    override fun logout() {
        LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }
}