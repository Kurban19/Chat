package com.envyglit.chat.data.repository

import androidx.lifecycle.MutableLiveData
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.user.User
import com.google.firebase.firestore.ListenerRegistration

class RepositoryImpl(private val fireBaseService: FireBaseService) : Repository {

    val users = MutableLiveData<List<User>>(listOf())

    override fun sendMessage(message: BaseMessage, chatId: String) {
        fireBaseService.sendMessage(message, chatId)
    }

    override fun findUserById(userId: String): User {
        val user = users.value?.find { it.id == userId }
        user ?: throw KotlinNullPointerException("User not found")
        return user
    }

    override fun addMessagesListener(
        chatId: String,
        onListen: (List<BaseMessage>) -> Unit
    ): ListenerRegistration {
        return fireBaseService.setChatMessagesListener(chatId, onListen)
    }
}
