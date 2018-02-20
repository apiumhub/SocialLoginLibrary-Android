package com.apiumhub.social

import android.content.Context


data class SocialNetworkConfiguration(val identifier: String)
data class SocialUserInformation(val userId: String, val token: String)


interface SocialManager {
    fun login(context: Context)
    fun logout()
}

interface SocialResponse {
    fun success(func: (user: SocialUserInformation) -> Unit)
    fun error(func: (error: Throwable) -> Unit)
}