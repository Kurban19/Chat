package com.shkiper.chat.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.shkiper.chat.ui.main.MainActivity
import com.shkiper.chat.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
