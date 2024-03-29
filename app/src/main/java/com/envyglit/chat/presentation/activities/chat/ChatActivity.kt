package com.envyglit.chat.presentation.activities.chat

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
import com.envyglit.chat.databinding.ActivityChatBinding
import com.envyglit.core.ui.extensions.toUser
import com.envyglit.chat.presentation.adapters.MessagesAdapter
import com.envyglit.chat.presentation.activities.main.MainActivity
//import com.envyglit.core.ui.glide.GlideApp
import com.envyglit.core.utils.StorageUtils
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.message.ImageMessage
import com.envyglit.core.domain.entities.message.TextMessage
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

    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

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
            binding.tvTitleChat.text = title

            if (avatar == null) {
//                GlideApp.with(this@ChatActivity)
//                    .clear(binding.ivAvatarChat)
                binding.ivAvatarChat.setInitials(initials)
            } else {
//                GlideApp.with(this@ChatActivity)
//                    .load(StorageUtils.pathToReference(avatar!!))
//                    .into(binding.ivAvatarChat)
            }
        }

        if (chat.isSingle()) {
            chat.members.find { it.id != FirebaseAuth.getInstance().currentUser?.uid }?.apply {
                binding.tvLastActivity.text =
                    viewModel.findUserById(this.id).toUserItem().lastActivity
            }
        } else {
            var concatenatedString = ""
            chat.members.forEach {
                if (it.id != FirebaseAuth.getInstance().currentUser?.uid) {
                    concatenatedString += it.firstName + " "
                }
            }
            binding.tvLastActivity.text = concatenatedString.trim()
        }

        binding.ivSend.setOnClickListener {
            if (binding.etMessage.text.toString() == "") {
                return@setOnClickListener
            }

            val message = TextMessage.makeMessage(
                binding.etMessage.text.toString(),
                FirebaseAuth.getInstance().currentUser!!.toUser()
            )

            if (chat.members.size > 2) {
                message.group = true
            }

            binding.etMessage.setText("")
            chat.lastMessage = message
            viewModel.sendMessage(message, chat.id)
            viewModel.update(chat)
        }

        binding.fabSelectImage.setOnClickListener {
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
        binding.rvMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messagesAdapter.apply {
                updateData(messages)
            }
        }

        binding.rvMessages.scrollToPosition(binding.rvMessages.adapter!!.itemCount - 1)
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        messagesListenerRegistration.remove()
    }
}