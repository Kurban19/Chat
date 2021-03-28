package com.shkiper.chat.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.models.data.User
import com.shkiper.chat.repositories.MainRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(
        private val mainRepository: MainRepository
        ) : ViewModel(){


    fun getChat(chatId: String): Chat {
        return mainRepository.find(chatId)
    }

    fun addChatMessagesListener(chatId: String, onListen: (List<TextMessage>) -> Unit): ListenerRegistration {
        return mainRepository.addMessagesListener(chatId, onListen)
    }

    fun sendMessage(message: TextMessage, chatId: String){
        mainRepository.sendMessage(message, chatId)
    }


}