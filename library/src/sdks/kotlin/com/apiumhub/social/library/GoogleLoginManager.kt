package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.auth.api.signin.*

class GoogleLoginManager(
        googleConfiguration: GoogleConfiguration,
        override val fragment: androidx.fragment.app.Fragment? = null,
        override val activity: Activity? = null) : SocialManager {

    init {
        if (fragment == null && activity == null) {
            throw IllegalArgumentException("Either a fragment or an activity must be provided on the constructor")
        }
        else if (fragment != null && activity != null) {
            throw IllegalArgumentException("Provide only a fragment OR an activity")
        }
    }

    private var mGoogleApiClient: GoogleApiClient? = null
    private val mGoogleConfiguration: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(googleConfiguration.clientId)
            .build()

    override fun login() {

        mGoogleApiClient = GoogleApiClient.Builder(
                activity ?: fragment?.context!!)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleConfiguration)
                .build()

        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        when {
            activity != null -> activity.startActivityForResult(intent, 1234)
            fragment != null -> fragment.startActivityForResult(intent, 1234)
            else -> throw IllegalStateException("Both activity and fragment are null")
        }
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
                if (it?.id != null && it.idToken != null && it.email != null) {
                    success(SocialUserInformation(it.id!!, it.idToken!!, it.email!!))
                }
                else {
                    error(SocialLoginException(SocialLoginErrorType.FAILED))
                }
            }
        }
    }
}

