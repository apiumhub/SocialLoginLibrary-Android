package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent


public interface SocialManager {
    val activity: Activity
    fun login(activity: Activity)
    fun logout()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit)
}