package ru.skillbranch.chat.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import ru.skillbranch.chat.ui.main.MainActivity
import ru.skillbranch.chat.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
//            FireBase.getAllDataFromServer{startActivity(Intent(this, MainActivity::class.java))}
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish()
    }
}
