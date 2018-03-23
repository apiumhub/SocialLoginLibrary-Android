package com.apiumhub.social.library

data class FacebookConfiguration(val permissions: List<String>) : SocialNetworkConfiguration
data class GoogleConfiguration(val clientId: String, val scopes: List<String>) : SocialNetworkConfiguration
