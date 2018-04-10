package com.apiumhub

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.apiumhub.databinding.ActivityMainBinding
import com.apiumhub.social.library.*

enum class SocialNetworkType {
    FACEBOOK, GOOGLE
}

class MainActivity : AppCompatActivity() {

    private val facebookLoginManager = FacebookLoginManager(FacebookConfiguration(listOf("public_profile", "email", "user_friends")), this)

    private val googleLoginManager = GoogleLoginManager(GoogleConfiguration("273380565662-mop5c2flpee0ch65kjsuotl2hio88dvp.apps.googleusercontent.com", listOf("")), this)

    private lateinit var flow: SocialNetworkType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.activityMainFacebookLogin.setOnClickListener {
            flow = SocialNetworkType.FACEBOOK
            facebookLoginManager.login(this)
        }

        binding.activityMainGoogleLogin.setOnClickListener {
            flow = SocialNetworkType.GOOGLE
            googleLoginManager.login(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (flow) {
            SocialNetworkType.FACEBOOK -> facebookLoginManager.onActivityResult(requestCode, resultCode, data, { userInfo: SocialUserInformation ->
                println(userInfo.token)
                println(userInfo.email)
            }) { error: SocialLoginException ->
                when (error.loginError) {
                    SocialLoginErrorType.CANCELED -> println("CANCELED")
                    SocialLoginErrorType.FAILED -> println("FAILED")
                    else -> println("OTHER ERRORS")
                }
            }
            SocialNetworkType.GOOGLE -> googleLoginManager.onActivityResult(requestCode, resultCode, data, { userInfo: SocialUserInformation ->
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
