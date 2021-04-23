package com.shkiper.chat.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shkiper.chat.R
import com.shkiper.chat.utils.FireBaseUtils
import com.shkiper.chat.utils.StorageUtils
import com.shkiper.chat.utils.Utils
import kotlinx.android.synthetic.main.activity_profile.*

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

    }


    private fun initToolbar(){

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