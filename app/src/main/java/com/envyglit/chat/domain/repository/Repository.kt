package com.envyglit.chat.domain.repository

import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.user.User
import com.google.firebase.firestore.ListenerRegistration

interface Repository {

    fun findUserById(userId: String): User
    fun sendMessage(message: BaseMessage, chatId: String)
    fun addMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration
}