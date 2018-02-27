package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import com.facebook.login.LoginManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.login.LoginResult
import com.facebook.FacebookException

data class FacebookConfiguration(val appId: String, val permissions: List<String>) : SocialNetworkConfiguration


class FacebookLoginManager(private val facebookConfiguration: FacebookConfiguration, public override val activity: Activity) : SocialManager {

    private var callbackManager: CallbackManager? = null

    override fun login(activity: Activity) {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().logInWithReadPermissions(this.activity, facebookConfiguration.permissions)
    }

    override fun logout() {
        LoginManager.getInstance().logOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        success(SocialUserInformation(loginResult.accessToken.userId, loginResult.accessToken.token))
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
}