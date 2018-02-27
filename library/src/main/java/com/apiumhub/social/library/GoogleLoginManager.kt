package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.signin.*


data class GoogleConfiguration(val clientId: String, val scopes: List<String>) : SocialNetworkConfiguration


public class GoogleLoginManager(private val googleConfiguration: GoogleConfiguration, public override val activity: Activity) : SocialManager {

    private var mGoogleApiClient: GoogleApiClient? = null
    private val mGoogleConfiguration: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(googleConfiguration.clientId)
            .build()

    override fun login(activity: Activity) {
        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleConfiguration)
                .build()

        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        activity.startActivityForResult(intent, 1234)
    }

    override fun logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {

        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (!result.isSuccess || result.signInAccount == null) {
            error(SocialLoginException(SocialLoginErrorType.FAILED))
        }
        else {
            result.signInAccount.let {
                if (it?.id != null && it.idToken != null) {
                    success(SocialUserInformation(it.id!!, it.idToken!!))
                }
                else {
                    error(SocialLoginException(SocialLoginErrorType.FAILED))
                }
            }
        }
    }
}

