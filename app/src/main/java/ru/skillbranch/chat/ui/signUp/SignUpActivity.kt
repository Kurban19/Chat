package ru.skillbranch.chat.ui.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.repositories.UserRepository
import ru.skillbranch.chat.ui.main.MainActivity

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        setContentView(R.layout.activity_login)

        initToolbar()


        btn_sigh_up.setOnClickListener{
            performSignUP()
        }

    }
    private fun performSignUP(){
        val name = et_name.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()

        if(email.isBlank() || password.isBlank() || name.isBlank()){
            Toast.makeText(this@SignUpActivity, "Check that you filled all the fields", Toast.LENGTH_LONG).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener

                    val user = User(it.result!!.user!!.uid, name, "")
                    UserRepository.addUser(user)

                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener{
                    Toast.makeText(this@SignUpActivity, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar_sign_up)
    }
}