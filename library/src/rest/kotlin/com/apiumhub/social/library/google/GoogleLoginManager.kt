package com.apiumhub.social.library.google

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.apiumhub.social.library.SocialLoginException
import com.apiumhub.social.library.SocialManager
import com.apiumhub.social.library.SocialUserInformation

class GoogleLoginManager(override val activity: Activity): SocialManager {
    override val fragment: Fragment?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun login() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}