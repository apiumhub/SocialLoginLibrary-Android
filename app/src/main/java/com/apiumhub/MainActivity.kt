package com.apiumhub

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.apiumhub.databinding.ActivityMainBinding
import com.apiumhub.social.library.*


class MainActivity : AppCompatActivity() {

    private val facebookLoginManager = FacebookLoginManager(FacebookConfiguration("179933089418674", listOf("public_profile", "email", "user_friends")))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        binding.activityMainFacebookLogin.setOnClickListener {

            facebookLoginManager.login(this, {
                // SUCCESS
                userInfo: SocialUserInformation ->
                println(userInfo.token)
            }) {
                // FAILURE
                error: SocialLoginException ->
                when(error.loginError) {
                     SocialLoginErrorType.CANCELED -> println("CANCELED")
                     SocialLoginErrorType.FAILED -> println("FAILED")
                    else -> println("OTHER ERRORS")
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        facebookLoginManager.onActivityResult(requestCode, resultCode, data)
    }

}
