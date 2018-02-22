package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent


public interface SocialManager {

    fun login( activity: Activity, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit)
    fun logout()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}