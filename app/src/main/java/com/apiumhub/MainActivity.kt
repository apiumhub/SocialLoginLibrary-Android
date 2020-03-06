package com.apiumhub

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import com.apiumhub.social.library.*
import com.apiumhub.social.library.SocialLoginErrorType.*
import kotlinx.android.synthetic.main.activity_main.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

enum class SocialNetworkType {
  FACEBOOK, GOOGLE
}

class MainActivity : AppCompatActivity() {

  private val facebookLoginManager = FacebookLoginManager(
    FacebookConfiguration(listOf("public_profile", "email", "user_friends")), activity = this
  )

  private val googleLoginManager = GoogleLoginManager(
    GoogleConfiguration("273380565662-mop5c2flpee0ch65kjsuotl2hio88dvp.apps.googleusercontent.com", listOf("")), activity = this
  )

  private lateinit var flow: SocialNetworkType

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    activity_main_facebook_login.setOnClickListener {
      flow = SocialNetworkType.FACEBOOK
      facebookLoginManager.login()
    }

    activity_main_google_login.setOnClickListener {
      flow = SocialNetworkType.GOOGLE
      googleLoginManager.login()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    when (flow) {
      SocialNetworkType.FACEBOOK -> facebookLoginManager.onActivityResult(requestCode, resultCode, data,
        { userInfo: SocialUserInformation ->
          println(userInfo.token)
          println(userInfo.email)
        }, { error: SocialLoginException ->
          when (error.loginError) {
            CANCELED -> println("CANCELED")
            FAILED -> println("FAILED")
            NO_EMAIL -> println("NO EMAIL")
            else -> println("OTHER ERRORS")
          }
        })
      SocialNetworkType.GOOGLE -> googleLoginManager.onActivityResult(requestCode, resultCode, data,
        { userInfo: SocialUserInformation ->
          println(userInfo.token)
          println(userInfo.email)
        }) { error: SocialLoginException ->
        when (error.loginError) {
          SocialLoginErrorType.CANCELED -> println("CANCELED")
          SocialLoginErrorType.FAILED -> println("FAILED")
          else -> println("OTHER ERRORS")
        }
      }
    }
  }
}
