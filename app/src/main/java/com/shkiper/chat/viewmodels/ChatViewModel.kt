package com.shkiper.chat.viewmodels

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import com.shkiper.chat.repositories.MainRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(
        private val mainRepository: MainRepository
        ) : ViewModel(){


    fun getChat(chatId: String): Chat {
        return mainRepository.findChat(chatId)
    }

    fun addChatMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration {
        return mainRepository.addMessagesListener(chatId, onListen)
    }

    fun sendMessage(message: BaseMessage, chatId: String){
        mainRepository.sendMessage(message, chatId)
    }

    fun update(chat: Chat){
        mainRepository.update(chat)
    }

    fun findUser(userId: String): User? {
        return mainRepository.findUser(userId)
    }

    fun updateData() {
        mainRepository.updateData()
    }


}