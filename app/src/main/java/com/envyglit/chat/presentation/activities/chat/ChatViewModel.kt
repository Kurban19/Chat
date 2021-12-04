package com.envyglit.chat.presentation.activities.chat

import androidx.lifecycle.ViewModel
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.domain.entities.chat.Chat
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
        private val repository: Repository
        ) : ViewModel(){


    fun getChat(chatId: String): Chat {
        return repository.findChatById(chatId)!! //TODO change to nullable
    }

    fun addChatMessagesListener(chatId: String, onListen: (List<com.envyglit.core.domain.entities.message.BaseMessage>) -> Unit): ListenerRegistration {
        return repository.addMessagesListener(chatId, onListen)
    }

    fun sendMessage(message: com.envyglit.core.domain.entities.message.BaseMessage, chatId: String){
        repository.sendMessage(message, chatId)
    }

    fun update(chat: Chat){
        repository.updateChat(chat)
    }

    fun findUserById(userId: String): com.envyglit.core.domain.entities.user.User {
        return repository.findUserById(userId)!!
    }

}