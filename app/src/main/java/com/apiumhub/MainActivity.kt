package com.apiumhub

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import com.apiumhub.databinding.ActivityMainBinding
import com.apiumhub.social.library.FacebookConfiguration
import com.apiumhub.social.library.FacebookLoginManager
import com.apiumhub.social.library.GoogleConfiguration
import com.apiumhub.social.library.GoogleLoginManager
import com.apiumhub.social.library.LinkedinLoginManager
import com.apiumhub.social.library.LinkedinLoginManager.LinkedinConfigurationBuilder
import com.apiumhub.social.library.SocialLoginErrorType
import com.apiumhub.social.library.SocialLoginErrorType.CANCELED
import com.apiumhub.social.library.SocialLoginErrorType.FAILED
import com.apiumhub.social.library.SocialLoginErrorType.NO_EMAIL
import com.apiumhub.social.library.SocialLoginException
import com.apiumhub.social.library.SocialUserInformation
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

enum class SocialNetworkType {
    FACEBOOK, GOOGLE, LINKEDIN
}

class MainActivity : AppCompatActivity() {

    private val facebookLoginManager = FacebookLoginManager(
        FacebookConfiguration(listOf("public_profile", "email", "user_friends")), activity = this)

    private val googleLoginManager = GoogleLoginManager(
        GoogleConfiguration("273380565662-mop5c2flpee0ch65kjsuotl2hio88dvp.apps.googleusercontent.com", listOf("")), activity = this)

    private lateinit var linkedinLoginManager: LinkedinLoginManager

    private lateinit var flow: SocialNetworkType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        linkedinLoginManager = LinkedinConfigurationBuilder.requestId().requestEmail().build(activity = this)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.activityMainFacebookLogin.setOnClickListener {
            flow = SocialNetworkType.FACEBOOK
            facebookLoginManager.login()
        }

        binding.activityMainGoogleLogin.setOnClickListener {
            flow = SocialNetworkType.GOOGLE
            googleLoginManager.login()
        }

        binding.activityMainLinkedinLogin.setOnClickListener {
            flow = SocialNetworkType.LINKEDIN
            linkedinLoginManager.login()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (flow) {
            SocialNetworkType.FACEBOOK -> facebookLoginManager.onActivityResult(requestCode, resultCode, data,
                { userInfo: SocialUserInformation ->
                    println(userInfo.token)
                    println(userInfo.email)
                }) { error: SocialLoginException ->
                when (error.loginError) {
                    CANCELED -> println("CANCELED")
                    FAILED -> println("FAILED")
                    NO_EMAIL -> println("NO EMAIL")
                    else -> println("OTHER ERRORS")
                }
            }
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
            SocialNetworkType.LINKEDIN -> linkedinLoginManager.onActivityResult(requestCode, resultCode, data,
                { userInfo: SocialUserInformation ->
                    println(userInfo.userId)
                    println(userInfo.email)
                }) { error: SocialLoginException ->
                when (error.loginError) {
                    SocialLoginErrorType.CANCELED -> println("CANCELED")
                    SocialLoginErrorType.FAILED -> println("FAILED ${error.originalError.toString()}")
                    else -> println("OTHER ERRORS")
                }
            }
        }

    }


    private fun getPackageHash() {
        try {

            @SuppressLint("PackageManagerGetSignatures") val info = packageManager.getPackageInfo(
                "com.apiumhub.social", //give your package name here
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())

                Log.d("linkedin",
                    "Hash  : " + Base64.encodeToString(md.digest(), Base64.NO_WRAP))//Key hash is printing in Log
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.d("linkedin", e.message, e)
        } catch (e: NoSuchAlgorithmException) {
            Log.d("linkedin", e.message, e)
        }

    }
}
