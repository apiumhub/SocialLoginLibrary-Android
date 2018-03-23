package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent

class FacebookLoginManager: SocialManager {
    override val activity: Activity
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun login(activity: Activity) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}