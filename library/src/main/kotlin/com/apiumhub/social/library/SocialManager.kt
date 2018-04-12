package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment

interface SocialManager {
    val activity: Activity?
    val fragment: Fragment?

    fun login()
    fun logout()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit)
}