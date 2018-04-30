package com.apiumhub.social.library

import com.linkedin.platform.utils.Scope

data class FacebookConfiguration(val permissions: List<String>) : SocialNetworkConfiguration
data class GoogleConfiguration(val clientId: String, val scopes: List<String>) : SocialNetworkConfiguration
data class LinkedinConfiguration(val scope: Scope, val userReqURL: String) : SocialNetworkConfiguration