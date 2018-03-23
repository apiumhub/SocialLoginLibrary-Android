package com.apiumhub.social.library


public interface SocialResponse {
    fun success(user: SocialUserInformation)
    fun error(error: Throwable)
}