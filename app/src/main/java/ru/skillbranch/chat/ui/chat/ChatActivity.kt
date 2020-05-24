package ru.skillbranch.chat.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.utils.AppConstants


class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initToolbar()

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.CHAT_NAME)
    }
}
