package com.admedia.login.Modules

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.startActivity
import com.admedia.login.Activities.HomeScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.internal.ContextUtils.getActivity


class GoogleLogin(var activity: Activity) : Activity() {

    companion object{
        val RC_SIGN_IN = 1000
    }

    fun signIn(){

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);

        val account = GoogleSignIn.getLastSignedInAccount(activity)

        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(activity, signInIntent, RC_SIGN_IN,null)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        this.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(activity)
            if (acct != null) {
                val personName = acct.displayName
                val personEmail = acct.email
                val newIntent = Intent(activity, HomeScreen::class.java)
                newIntent.putExtra(personName, personName)
                newIntent.putExtra(personEmail, personEmail)
                startActivity(activity, newIntent,null)
                toast("Google Success")
            }

        } catch (e: ApiException) {
            toast("Something Went Wrong")
        }
    }

    fun toast(text : String){
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }
}