package ru.skillbranch.chat.ui.chat

import android.annotation.SuppressLint
import android.os.Bundle
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
    private var reference: CollectionReference? = null
    private lateinit var messagesAdapter: MessagesAdapter
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
        chat.members.forEach{
            if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                tv_last_activity.text = it.toUserItem().lastActivity
            }
        }

        messagesAdapter = MessagesAdapter()

        FireBaseUtil.getMessages(chat.id){
            messagesAdapter.updateData(it)
        }

//        reference = FirebaseFirestore.getInstance().collection("chats").document(chat.id).collection("messages")
//        reference!!
//                .orderBy("time")
//                .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
//                    if (firebaseFirestoreException != null) {
//                        return@addSnapshotListener
//                    }
//
//                    val items = mutableListOf<TextMessage>()
//
//                    querySnapshot!!.documents.forEach{
//                        items.add(it.toObject(TextMessage::class.java)!!)
//                    }
//                    messagesAdapter.updateData(items)
//                }

        with(rv_messages){
            adapter = messagesAdapter
            layoutManager = LinearLayoutManager(this@ChatActivity)
            scrollToPosition(messagesAdapter.itemCount - 1)
        }

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

    private fun updateRecyclerView(messages: List<Item>){
        fun init(){
            recycler_view_messages.apply{
                layoutManager = LinearLayoutManager(this@ChatActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if(shouldInitRecyclerView)
            init()

        else
            updateItems()

        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)

    }


    private fun initToolbar() {
        setSupportActionBar(toolbar_chat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
