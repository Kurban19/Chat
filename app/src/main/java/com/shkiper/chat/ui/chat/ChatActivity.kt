package com.shkiper.chat.ui.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.activity_chat.*
import com.shkiper.chat.R
import com.shkiper.chat.extensions.toUser
import com.shkiper.chat.glide.GlideApp
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.ImageMessage
import com.shkiper.chat.model.TextMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.ui.adapters.MessagesAdapter
import com.shkiper.chat.ui.main.MainActivity
import com.shkiper.chat.utils.StorageUtils
import com.shkiper.chat.viewmodels.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.*


@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    companion object {
        private const val RC_SELECT_IMAGE = 2
    }

    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var chat: Chat
    private val messagesAdapter: MessagesAdapter = MessagesAdapter()
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initViews()
        setMessagesListener()
        initToolbar()
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
        chat = viewModel.getChat(intent.getStringExtra(MainActivity.CHAT_ID)!!)
        val chatItem = chat.toChatItem()

        with(chatItem) {
            tv_title_chat.text = title

            if(avatar == null){
                GlideApp.with(this@ChatActivity)
                        .clear(iv_avatar_chat)
                iv_avatar_chat.setInitials(initials)
            } else{
                GlideApp.with(this@ChatActivity)
                        .load(StorageUtils.pathToReference(avatar))
                        .into(iv_avatar_chat)
            }
        }

        if(chat.isSingle()){
            chat.members.find { it.id != FirebaseAuth.getInstance().currentUser!!.uid }?.apply {
                tv_last_activity.text = viewModel.findUser(this.id)?.toUserItem()?.lastActivity
            }
        } else{
            var concatenatedString = ""
            chat.members.forEach {
                if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                    concatenatedString +=it.firstName + " "
                }
            }
            tv_last_activity.text = concatenatedString.trim()
        }

        iv_send.setOnClickListener{
            if(et_message.text.toString() == ""){
                return@setOnClickListener
            }

            val message = TextMessage.makeMessage(et_message.text.toString(), FirebaseAuth.getInstance().currentUser!!.toUser())

            if (chat.members.size > 2){
                message.group = true
            }

            et_message.setText("")
            chat.lastMessage = message
            viewModel.sendMessage(message, chat.id)
            viewModel.update(chat)
        }

        fab_select_image.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
            }
            startActivityForResult(Intent.createChooser(intent, "Select Image"), RC_SELECT_IMAGE)
        }

    }

    private fun setMessagesListener(){
        messagesListenerRegistration = viewModel.addChatMessagesListener(chat.id, this::updateRecyclerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null) {
            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            StorageUtils.uploadMessageImage(selectedImageBytes) { imagePath ->
                val imageToSend = if(chat.members.size > 1) {
                    ImageMessage.makeMessage(imagePath, FirebaseAuth.getInstance().currentUser!!.toUser(), true)
                }
                else{
                    ImageMessage.makeMessage(imagePath, FirebaseAuth.getInstance().currentUser!!.toUser(), false)
                }

                chat.lastMessage = imageToSend
                viewModel.sendMessage(imageToSend, chat.id)
                viewModel.update(chat)
            }
        }
    }


    private fun updateRecyclerView(messages: List<BaseMessage>){
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