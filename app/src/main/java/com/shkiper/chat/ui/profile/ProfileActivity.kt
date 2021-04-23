package com.shkiper.chat.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.firebase.ui.auth.AuthUI
import com.shkiper.chat.R
import com.shkiper.chat.extensions.showToast
import com.shkiper.chat.ui.login.LoginActivity
import com.shkiper.chat.utils.FireBaseUtils
import com.shkiper.chat.utils.StorageUtils
import com.shkiper.chat.utils.Utils
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

            Glide.with(this)
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
                if (!pictureJustChanged && user.avatar != null)
                    Glide.with(this)
                            .load(StorageUtils.pathToReference(user.avatar))
                            .into(iv_profile_avatar)
                else{
                    Glide.with(this)
                            .clear(iv_profile_avatar)
                    iv_profile_avatar.setInitials(Utils.toInitials(user.firstName, user.lastName))

                }

        }
    }


}