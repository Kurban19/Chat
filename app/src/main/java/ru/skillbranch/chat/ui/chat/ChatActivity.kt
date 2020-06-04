package ru.skillbranch.chat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.data.managers.FireBaseUtil
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.BaseMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.ui.adapters.MessagesAdapter
import ru.skillbranch.chat.utils.AppConstants
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var currentChannelId: String
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

        with(rv_messages){
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
            scrollToPosition(messagesAdapter.itemCount - 1)
        }

        FireBaseUtil.getOrCreateChatChannel(chat.members.first().id){ channelId ->
            currentChannelId = channelId

            FireBaseUtil.addChatMessagesListener(channelId, this::updateRecycleView)

            messagesAdapter.updateData(FireBaseUtil.getMessages(currentChannelId))

            iv_send.setOnClickListener{
                val message = BaseMessage.makeMessage(FirebaseAuth.getInstance().currentUser!!.toUser(), chat, Date(), "text", et_message.text.toString(), false, isRead = true)
                et_message.setText("")
                //chat.messages.add(message)
                FireBaseUtil.sendMessage(message, channelId)
                ChatRepository.update(chat)
            }
        }

        //ChatRepository.update(chat)
    }

    private fun updateRecycleView(items: List<BaseMessage>){
        messagesAdapter.updateData(items)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
