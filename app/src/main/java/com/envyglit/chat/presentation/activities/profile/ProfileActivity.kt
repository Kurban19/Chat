package com.envyglit.chat.presentation.activities.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.envyglit.chat.databinding.ActivityProfileBinding
import com.envyglit.chat.util.extensions.showToast
import com.envyglit.chat.presentation.glide.GlideApp
import com.envyglit.chat.presentation.activities.login.LoginActivity
import com.envyglit.chat.util.FireBaseUtils
import com.envyglit.chat.util.StorageUtils
import com.envyglit.chat.util.Utils
import java.io.ByteArrayOutputStream

class ProfileActivity: AppCompatActivity() {

    private val RC_SELECT_IMAGE = 2

    private lateinit var selectedImageBytes: ByteArray

    private var pictureJustChanged = false

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        binding.ivProfileAvatar.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
        }


        binding.btnSave.setOnClickListener {
            if (::selectedImageBytes.isInitialized)
                StorageUtils.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                    FireBaseUtils.updateCurrentUser(
                        binding.editTextName.text.toString(),
                        binding.editTextSurname.text.toString(), imagePath
                    )
                }
            else
                FireBaseUtils.updateCurrentUser(
                    binding.editTextName.text.toString(),
                    binding.editTextSurname.text.toString(), null
                )

            showToast("Saving")
        }

        binding.btnSignOut.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
        }

    }

    private fun initToolbar(){
        setSupportActionBar(binding.toolbar)
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
                .into(binding.ivProfileAvatar)

            pictureJustChanged = true
        }
    }

    override fun onStart() {
        super.onStart()
        FireBaseUtils.getCurrentUser { user ->
            binding.editTextName.setText(user.firstName)
            binding.editTextSurname.setText(user.lastName)
            if (!pictureJustChanged && user.avatar != null) {
                GlideApp.with(this)
                    .load(StorageUtils.pathToReference(user.avatar.orEmpty()))
                    .into(binding.ivProfileAvatar)
            } else if (!pictureJustChanged) {
                GlideApp.with(this)
                    .clear(binding.ivProfileAvatar)
                binding.ivProfileAvatar.setInitials(Utils.toInitials(user.firstName, user.lastName))
            }

        }
    }

}
