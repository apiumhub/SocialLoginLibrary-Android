package com.apiumhub.social.library

data class SocialLoginException(val loginError: SocialLoginErrorType) : Throwable()