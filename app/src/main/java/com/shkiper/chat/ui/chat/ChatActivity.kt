package com.shkiper.chat.ui.chat

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.App
import kotlinx.android.synthetic.main.activity_chat.*
import com.shkiper.chat.R
import com.shkiper.chat.extensions.toUser
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.ui.adapters.MessagesAdapter
import com.shkiper.chat.ui.main.MainActivity
import com.shkiper.chat.viewmodels.ChatViewModel
import java.util.*
import javax.inject.Inject

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var chat: Chat
    private val messagesAdapter: MessagesAdapter = MessagesAdapter()
    @Inject
    lateinit var viewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        (applicationContext as App).appComponent.inject(this)
        initToolbar()
        initViews()
        setMessagesListener()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if(item?.itemId == android.R.id.home){
            finish()
            true
        }
        else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun initViews(){
        chat = viewModel.getChat(intent.getStringExtra(MainActivity.CHAT_ID)!!)
        val chatItem = chat.toChatItem()

        with(chatItem) {
            tv_title_chat.text = title
            if(avatar == null){
                Glide.with(this@ChatActivity)
                        .clear(iv_avatar_chat)
                iv_avatar_chat.setInitials(initials)
            } else{
                Glide.with(this@ChatActivity)
                        .load(avatar)
                        .into(iv_avatar_chat)
            }
        }

        if(chat.members.size > 2){
            var concatenatedString = ""
            chat.members.forEach {
                if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                    concatenatedString += it.firstName + " "
                }
            }
            tv_last_activity.text = concatenatedString.trim()
        }
        else{
            chat.members.forEach{
                if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                    tv_last_activity.text = chat.members.last().toUserItem().lastActivity
                }
            }
        }
    }

    private fun setMessagesListener(){
        messagesListenerRegistration = viewModel.addChatMessagesListener(chat.id, this::updateRecyclerView)

        iv_send.setOnClickListener{
            if(et_message.text.toString() == ""){
                return@setOnClickListener
            }
            val message = TextMessage.makeMessage(et_message.text.toString(), FirebaseAuth.getInstance().currentUser!!.toUser())
            et_message.setText("")
            chat.lastMessage = message

            viewModel.sendMessage(message, chat.id)

        }
    }

    private fun updateRecyclerView(messages: List<TextMessage>){
        rv_messages.apply{
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messagesAdapter.apply {
                updateData(messages)
                }
        }

        rv_messages.scrollToPosition(rv_messages.adapter!!.itemCount - 1)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        messagesListenerRegistration.remove()
    }
}
