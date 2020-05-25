package ru.skillbranch.chat.ui.chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.ui.adapters.MessagesAdapter
import ru.skillbranch.chat.utils.AppConstants
import ru.skillbranch.chat.viewmodels.ChatViewModel


class ChatActivity : AppCompatActivity() {

    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var viewModel: ChatViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initToolbar()
        initViews()
        initViewModel()
    }



    private fun initViews(){
        val chat = ChatRepository.find(intent.getStringExtra(AppConstants.CHAT_ID)!!)
        chat ?: return

        messagesAdapter = MessagesAdapter()

        messagesAdapter.updateData(chat.messages)

        with(rv_messages){
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
        }


    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(AppConstants.CHAT_NAME)
    }
}
