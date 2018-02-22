package com.apiumhub.social.library

import android.app.Activity
import android.content.Context
import android.content.Intent


data class GoogleConfiguration(val clientId: String, val scopes: List<String>): SocialNetworkConfiguration


public class GoogleLoginManager(private val googleConfiguration: GoogleConfiguration) : SocialManager {

    override fun login(activity: Activity, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {



    }

    override fun logout() {
      //  TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}