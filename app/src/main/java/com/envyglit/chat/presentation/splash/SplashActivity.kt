package com.envyglit.chat.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.envyglit.chat.R
import com.envyglit.chat.presentation.main.MainActivity
import com.envyglit.chat.presentation.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
