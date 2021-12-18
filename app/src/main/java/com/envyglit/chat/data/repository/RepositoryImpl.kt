package com.envyglit.chat.data.repository

import androidx.lifecycle.MutableLiveData
import com.envyglit.core.data.local.Database
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.user.User
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val fireBaseService: FireBaseService, private val database: Database) :
    Repository {

    val users = MutableLiveData<List<User>>(listOf())

    override fun getUsers(): Observable<List<User>> {
        return fireBaseService.getUsers()
            .doOnNext {
                users.value = it
            }
    }

    override fun findUserById(userId: String): User {
        val user = users.value?.find { it.id == userId }
        user ?: throw KotlinNullPointerException("User not found")
        return user
    }

    override fun findUsers(ids: List<String>): MutableList<User> {
        return users.value!!.filter { ids.contains(it.id) }.toMutableList()
    }

    override fun createChat(user: User) {
        fireBaseService.getOrCreateChat(user)
    }

    override fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }

    override fun sendMessage(message: BaseMessage, chatId: String) {
        fireBaseService.sendMessage(message, chatId)
    }

    override fun addMessagesListener(
        chatId: String,
        onListen: (List<BaseMessage>) -> Unit
    ): ListenerRegistration {
        return fireBaseService.setChatMessagesListener(chatId, onListen)
    }
}
