package com.admedia.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.admedia.login.Activities.HomeScreen
import com.admedia.login.Modules.GoogleLogin
import com.admedia.login.databinding.ActivityMainBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import java.util.*


class MainActivity : AppCompatActivity() {

    companion object {
        val RC_SIGN_IN = 1000
    }

    lateinit var binding: ActivityMainBinding

    //    val fbSignIn = FacebookLogin(this)
    val callbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        listeners()
    }

    private fun listeners() {
        binding.btnGoogle.setOnClickListener {
            signIn()
        }

        binding.btnFacebook.setOnClickListener {
            fbSignIn()
            LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("public_profile")
            )
        }

        /*binding.btnTwitter.setOnClickListener {
            twitterSignIn()
        }*/
    }

    fun signIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        val account = GoogleSignIn.getLastSignedInAccount(this)

        val signInIntent = mGoogleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleLogin.RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            if (acct != null) {
                val personName = acct.displayName
                val personEmail = acct.email

                val newIntent = Intent(this, HomeScreen::class.java)

                newIntent.putExtra("name", personName)
                newIntent.putExtra("email", personEmail)
                newIntent.putExtra("method", Constants.GOOGLE)

                startActivity(newIntent)

                toast("Google Success")

                finish()
            }

        } catch (e: ApiException) {
            toast("Something Went Wrong")
        }
    }

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
                    val newIntent = Intent(this@MainActivity, HomeScreen::class.java)
                    newIntent.putExtra("method", Constants.FACEBOOK)
                    startActivity(newIntent)
                }
            })
    }

    fun twitterSignIn(){

        val firebaseAuth = FirebaseAuth.getInstance()
        val provider = OAuthProvider.newBuilder("twitter.com")
        provider.addCustomParameter("lang", "fr")

        val pendingResultTask: Task<AuthResult> = firebaseAuth.getPendingAuthResult() as Task<AuthResult>
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                .addOnSuccessListener {
                    val twitterIntent = Intent(this@MainActivity, HomeScreen::class.java)
                    twitterIntent.putExtra("method", Constants.TWITTER)
                    startActivity(twitterIntent)
                }
                .addOnFailureListener {
                    toast("Something Went Wrong")
                }
        } else {
            firebaseAuth
                .startActivityForSignInWithProvider( /* activity= */this, provider.build())
                .addOnSuccessListener {
                    val twitterIntent = Intent(this@MainActivity, HomeScreen::class.java)
                    twitterIntent.putExtra("method", Constants.TWITTER)
                    startActivity(twitterIntent)
                }
                .addOnFailureListener {
                    toast("Something Went Wrong")
                }

        }

    }

    private fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}