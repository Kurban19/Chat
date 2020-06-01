package ru.skillbranch.chat.ui.signUp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import ru.skillbranch.chat.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import ru.skillbranch.chat.data.managers.FireBaseUtil
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.ui.main.MainActivity

class SignUpActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 0

    private val signInProviders =
            listOf(AuthUI.IdpConfig.EmailBuilder()
                    .setAllowNewAccounts(true)
                    .setRequireName(true)
                    .build())

    override fun onCreate(savedInstanceState: Bundle?) {
        //setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        if(FirebaseAuth.getInstance().currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            FirebaseAuth.getInstance().signOut()
            finish()

        }
        setContentView(R.layout.activity_login)

        initToolbar()


        btn_sigh_up.setOnClickListener{
            val intent = AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(signInProviders)
                    .setLogo(R.mipmap.ic_launcher)
                    .build()
            startActivityForResult(intent,RC_SIGN_IN)

            //performSignUP()
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

                    val user = User(it.result!!.user!!.uid, name, "", email)
                    FireBaseUtil.addUser(user)

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {

                FireBaseUtil.initCurrentUserIfFirstTime {
                    startActivity(Intent(this, MainActivity::class.java))
                }
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