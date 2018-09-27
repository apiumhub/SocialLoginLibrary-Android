package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

interface SocialManager {
    val activity: Activity?
    val fragment: androidx.fragment.app.Fragment?

    fun login()
    fun logout()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit)
}