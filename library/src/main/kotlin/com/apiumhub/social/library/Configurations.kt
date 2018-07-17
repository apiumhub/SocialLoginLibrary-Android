package com.apiumhub.social.library

import com.linkedin.platform.utils.Scope.LIPermission

data class WeChatConfiguration(val appId: String, val permissions: List<String>): SocialNetworkConfiguration
data class FacebookConfiguration(val permissions: List<String>) : SocialNetworkConfiguration
data class GoogleConfiguration(val clientId: String, val scopes: List<String>) : SocialNetworkConfiguration
internal data class LinkedinConfiguration(val permissions: List<String>, val scopes: Set<LIPermission>) : SocialNetworkConfiguration
