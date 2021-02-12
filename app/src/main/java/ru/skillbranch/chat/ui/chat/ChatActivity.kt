package ru.skillbranch.chat.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import ru.skillbranch.chat.R
import ru.skillbranch.chat.firebase.FireBase
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.repositories.UsersRepository
import ru.skillbranch.chat.ui.adapters.MessagesAdapter
import ru.skillbranch.chat.ui.main.MainActivity
import ru.skillbranch.chat.ui.profile.ProfileActivity
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chat: Chat
    private val messagesAdapter: MessagesAdapter = MessagesAdapter()

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

    private fun initViews(){
        chat = ChatRepository.find(intent.getStringExtra(MainActivity.CHAT_ID)!!)
        val chatItem = chat.toChatItem()
        chatItem.messageCount = 0

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

        iv_avatar_chat.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        chat.members.forEach{
            if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                tv_last_activity.text = UsersRepository.findUser(it.id).toUserItem().lastActivity
            }
        }

        FireBase.addChatMessagesListener(chat.id, this::updateRecyclerView)

        iv_send.setOnClickListener{
            if(et_message.text.toString() == ""){
                return@setOnClickListener
            }
            val message = TextMessage.makeMessage(et_message.text.toString(), FirebaseAuth.getInstance().currentUser!!.toUser())
            et_message.setText("")
            chat.lastMessage = message

            FireBase.sendMessage(message, chat.id)
            FireBase.updateChat(chat)

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

}
