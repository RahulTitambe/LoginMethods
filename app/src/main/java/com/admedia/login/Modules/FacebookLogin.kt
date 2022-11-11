package com.admedia.login.Modules

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.admedia.login.Activities.HomeScreen
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

class FacebookLogin(context: Context) : Activity() {


    val callbackManager = CallbackManager.Factory.create()
    val newIntent = Intent(context, HomeScreen::class.java)

    fun fbSignIn() {

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onCancel() {
                    toast("Something Went Wrong")
                }

                override fun onError(error: FacebookException) {
                    toast("Something Went Wrong")
                }

                override fun onSuccess(result: LoginResult) {
                    newIntent.putExtra("method", "Facebook")
                    startActivity(newIntent)
                }

            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun toast(text: String) {
        Toast.makeText(
            applicationContext,
            text, Toast.LENGTH_LONG
        ).show()
    }

}