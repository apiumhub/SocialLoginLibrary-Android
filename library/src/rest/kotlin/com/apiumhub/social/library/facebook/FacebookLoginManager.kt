package com.apiumhub.social.library.facebook

import android.app.Activity
import android.content.Intent
import com.apiumhub.social.library.SocialLoginException
import com.apiumhub.social.library.SocialManager
import com.apiumhub.social.library.SocialUserInformation

class FacebookLoginManager(override val activity: Activity): SocialManager {

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