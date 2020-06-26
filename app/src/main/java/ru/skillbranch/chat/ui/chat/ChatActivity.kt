package ru.skillbranch.chat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.data.managers.FireBaseUtil
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.ui.adapters.MessagesAdapter
import ru.skillbranch.chat.utils.AppConstants
import java.util.*

class ChatActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var messagesAdapter: MessagesAdapter
    private var shouldInitRecyclerView = true
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var chat: Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        initToolbar()
        initViews()
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

    @SuppressLint("SetTextI18n")
    private fun initViews(){
        FireBaseUtil.getMessages()
        chat = ChatRepository.find(intent.getStringExtra(AppConstants.CHAT_ID)!!)!!
        val chatItem = chat.toChatItem()

//        for(baseMessage in chat.messages){
//            baseMessage.isRead = true
//        }

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
//        chat.members.forEach{
//            if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
//                tv_last_activity.text = it.toUserItem().lastActivity
//            }
//        }

        messagesListenerRegistration = FireBaseUtil.addChatMessagesListener(chat.id, this::updateRecyclerView)

        iv_send.setOnClickListener{
            if(et_message.text.toString() == ""){
                return@setOnClickListener
            }
            val message = TextMessage("3kl2ej32sf23", FirebaseAuth.getInstance().currentUser!!.toUser(), isRead = false, isIncoming = false, date = Date(), type = "text", text = et_message.text.toString())
            et_message.setText("")
            chat.messages.add(message)

            FireBaseUtil.sendMessage(message, chat.id)
            FireBaseUtil.sendMessageChat(chat)

        }
    }

    private fun updateRecyclerView(messages: List<TextMessage>){
        fun init(){
            rv_messages.apply{
                layoutManager = LinearLayoutManager(this@ChatActivity)
                Log.d(TAG, "init adapter")
                adapter = MessagesAdapter().apply {
                    updateData(messages)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesAdapter.updateData(messages)

        if(shouldInitRecyclerView)
            init()
        else
            updateItems()

        rv_messages.scrollToPosition(rv_messages.adapter!!.itemCount - 1)
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}
