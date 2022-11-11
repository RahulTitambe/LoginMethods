package com.admedia.login.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.admedia.login.Constants
import com.admedia.login.databinding.HomeScreenBinding
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.GraphResponse
import org.json.JSONObject

class HomeScreen : AppCompatActivity() {

    lateinit var binding: HomeScreenBinding
    var accessToken = AccessToken.getCurrentAccessToken()
    lateinit var fullName : String
    lateinit var loginMethod : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setText()
    }

    private fun setText(){

        loginMethod = intent.getStringExtra("method").toString()

        if(loginMethod == Constants.FACEBOOK){

            val request = GraphRequest.newMeRequest(
                accessToken,
                object : GraphRequest.GraphJSONObjectCallback {
                    override fun onCompleted(
                        obj: JSONObject?,
                        response: GraphResponse?
                    ) {
                        if (obj != null) {
                            fullName = obj.getString("name")
                            binding.txtName.text = "Hello, " + fullName
                        }
                    }
                })

            val parameters = Bundle()
            parameters.putString("fields", "id,name,link")
            request.parameters = parameters
            request.executeAsync()

        }

        if(loginMethod == Constants.GOOGLE){
            binding.txtName.text = "Hello, " + intent.getStringExtra("name")
            binding.txtEmail.text = intent.getStringExtra("email")
        }

        if(loginMethod == Constants.TWITTER){

        }

        binding.txtLoginMethod.text = "You have loged in with " + intent.getStringExtra("method")

    }
}