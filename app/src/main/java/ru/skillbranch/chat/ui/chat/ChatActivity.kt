package ru.skillbranch.chat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.data.managers.CacheManager
import ru.skillbranch.chat.data.managers.FireBaseUtil
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.ui.adapters.MessagesAdapter
import ru.skillbranch.chat.utils.AppConstants
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messagesAdapter: MessagesAdapter
    private lateinit var chat: Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initToolbar()
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        chat = ChatRepository.find(intent.getStringExtra(AppConstants.CHAT_ID)!!)!!
        val chatItem = chat.toChatItem()

        for(baseMessage in chat.messages){
            baseMessage.isRead = true
        }

        with(chatItem) {
            tv_title_chat.text = " $title"
            if(avatar == null){
                Glide.with(this@ChatActivity)
                        .clear(iv_avatar_chat)
                iv_avatar_chat.setInitials(initials)
            }
            else{
                Glide.with(this@ChatActivity)
                        .load(avatar)
                        .into(iv_avatar_chat)
            }
        }


        messagesAdapter = MessagesAdapter()
        messagesAdapter.updateData(chat.messages)

        with(rv_messages){
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
            scrollToPosition(messagesAdapter.itemCount - 1)
        }

        iv_send.setOnClickListener{
            val message = TextMessage("3kl2ej32sf23", FirebaseAuth.getInstance().currentUser!!.toUser(), false, false, Date(), "text", et_message.text.toString())
            et_message.setText("")
            chat.messages.add(message)

            FireBaseUtil.sendMessageChat(chat)
            CacheManager.update(chat)

            messagesAdapter.updateData(chat.messages)

        }

        FireBaseUtil.sendMessageChat(chat)
        CacheManager.update(chat)
    }


    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
