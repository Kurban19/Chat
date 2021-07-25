package com.shkiper.chat.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.shkiper.chat.R
import com.shkiper.chat.databinding.ActivityLoginBinding
import com.shkiper.chat.presentation.main.MainActivity
import com.shkiper.chat.util.FireBaseUtils

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 0

    private val signInProviders =
        listOf(
            AuthUI.IdpConfig.EmailBuilder()
                .setAllowNewAccounts(true)
                .setRequireName(true)
                .build()
        )

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        initToolbar()

        binding.btnSighUp.setOnClickListener {
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(signInProviders)
                .setLogo(R.mipmap.ic_launcher)
                .build()
            startActivityForResult(intent, RC_SIGN_IN)
        }

    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                FireBaseUtils.initCurrentUserIfFirstTime()
                startActivity(Intent(this, MainActivity::class.java))
            } else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when(response.error?.errorCode){
                    ErrorCodes.NO_NETWORK ->
                        Toast.makeText(this, "No network", Toast.LENGTH_LONG).show()
                    ErrorCodes.UNKNOWN_ERROR ->
                        Toast.makeText(this, "Unknown error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}