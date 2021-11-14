package com.envyglit.chat.presentation.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.envyglit.chat.R
import com.envyglit.chat.presentation.activities.main.MainActivity
import com.envyglit.chat.presentation.activities.login.LoginActivity
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
