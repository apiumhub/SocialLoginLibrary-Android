package com.apiumhub.social.library

import android.app.Activity
import android.content.Intent
import android.support.v4.app.Fragment
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory

class WeChatLoginManager(
        private val weChatConfiguration: WeChatConfiguration,
        override val fragment: Fragment? = null,
        override val activity: Activity? = null) : SocialManager {

    private val wxApi = WXAPIFactory.createWXAPI(activity ?: fragment?.activity, weChatConfiguration.appId, false)

    init {
        if (fragment == null && activity == null) {
            throw IllegalArgumentException("Either a fragment or an activity must be provided on the constructor")
        }
        else if (fragment != null && activity != null) {
            throw IllegalArgumentException("Provide only a fragment OR an activity")
        }
    }

    override fun login() {
        val req = SendAuth.Req()
        req.scope = weChatConfiguration.permissions.joinToString(",")
        req.state = "xxx"
        wxApi.sendReq(req)
    }

    override fun logout() {
        //NoOp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, success: (user: SocialUserInformation) -> Unit, error: (error: SocialLoginException) -> Unit) {
        //NoOp
    }
}