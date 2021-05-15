package com.shkiper.chat.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.shkiper.chat.R
import com.shkiper.chat.extensions.showToast
import com.shkiper.chat.glide.GlideApp
import com.shkiper.chat.ui.login.LoginActivity
import com.shkiper.chat.util.FireBaseUtils
import com.shkiper.chat.util.StorageUtils
import com.shkiper.chat.util.Utils
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream

class ProfileActivity: AppCompatActivity() {

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initToolbar()
        initViews()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home){
            finish()
            true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }


    private fun initViews(){

        iv_profile_avatar.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
        }


        btn_save.setOnClickListener {
            if (::selectedImageBytes.isInitialized)
                StorageUtils.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                    FireBaseUtils.updateCurrentUser(editText_name.text.toString(),
                            editText_surname.text.toString(), imagePath)
                }
            else
                FireBaseUtils.updateCurrentUser(editText_name.text.toString(),
                        editText_surname.text.toString(), null)

            showToast("Saving")
        }


        btn_sign_out.setOnClickListener {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
        }

    }


    private fun initToolbar(){
        setSupportActionBar(toolbar_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            val selectedImagePath = data.data
            val selectedImageBmp = MediaStore.Images.Media
                    .getBitmap(this.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            selectedImageBytes = outputStream.toByteArray()


            GlideApp.with(this)
                    .load(selectedImageBytes)
                    .into(iv_profile_avatar)

            pictureJustChanged = true
        }
    }



    override fun onStart() {
        super.onStart()
        FireBaseUtils.getCurrentUser { user ->
            editText_name.setText(user.firstName)
            editText_surname.setText(user.lastName)
                if (!pictureJustChanged && user.avatar != null) {
                    GlideApp.with(this)
                            .load(StorageUtils.pathToReference(user.avatar!!))
                            .into(iv_profile_avatar)
                }
                else if(!pictureJustChanged){
                    GlideApp.with(this)
                            .clear(iv_profile_avatar)
                    iv_profile_avatar.setInitials(Utils.toInitials(user.firstName, user.lastName))
                }

        }
    }


}
