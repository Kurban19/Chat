package com.shkiper.chat.presentation.chat

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.domain.entities.BaseMessage
import com.shkiper.chat.domain.entities.data.Chat
import com.shkiper.chat.domain.entities.data.User
import dagger.hilt.android.lifecycle.HiltViewModel
import com.shkiper.chat.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
        private val repository: Repository
        ) : ViewModel(){


    fun getChat(chatId: String): Chat {
        return repository.findChatById(chatId)
    }

    fun addChatMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration {
        return repository.addMessagesListener(chatId, onListen)
    }

    fun sendMessage(message: BaseMessage, chatId: String){
        repository.sendMessage(message, chatId)
    }

    fun update(chat: Chat){
        repository.updateChat(chat)
    }

    fun findUserById(userId: String): User {
        return repository.findUserById(userId)
    }

}