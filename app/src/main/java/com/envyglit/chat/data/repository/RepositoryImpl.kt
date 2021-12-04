package com.envyglit.chat.data.repository

import androidx.lifecycle.MutableLiveData
import com.envyglit.chat.data.local.Database
import com.envyglit.chat.data.remote.FireBaseService
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.domain.entities.chat.Chat
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val fireBaseService: FireBaseService, private val database: Database) :
    Repository {

    val chats = MutableLiveData<List<Chat>>(listOf())
    val users = MutableLiveData<List<com.envyglit.core.domain.entities.user.User>>(listOf())

    override fun getUsers(): Observable<List<com.envyglit.core.domain.entities.user.User>> {
        return fireBaseService.getUsers()
            .doOnNext {
                users.value = it
            }
    }

    override fun getChats(): Observable<List<Chat>> {
//        return Observable.concat(
//            getChatsFromDb(),
//            getChatsFromApi())
        return fetchChatsFromApi()
    }

    private fun getChatsFromDb(): Observable<List<Chat>> {
        return database.chatDao().getChats().filter { it.isNotEmpty() }
    }

    private fun fetchChatsFromApi(): Observable<List<Chat>> {
        return fireBaseService.getEngagedChats()
            .doOnNext() {
//                storeChatsInDb(it)
                chats.value = it
            }
    }

    private fun storeChatsInDb(chats: List<Chat>) {
        database.chatDao().insertAllChats(chats)
    }

    override fun findUserById(userId: String): com.envyglit.core.domain.entities.user.User {
        val user = users.value?.find { it.id == userId }
        user ?: throw KotlinNullPointerException("User not found")
        return user
    }

    override fun findChatById(chatId: String): Chat {
        val ind = chats.value?.indexOfFirst { it.id == chatId } ?: 0
        val chat = chats.value?.get(ind)
        chat ?: throw KotlinNullPointerException("Chat not found")
        return chat
    }

    override fun findUsers(ids: List<String>): MutableList<com.envyglit.core.domain.entities.user.User> {
        return users.value!!.filter { ids.contains(it.id) }.toMutableList()
    }

    override fun createChat(user: com.envyglit.core.domain.entities.user.User) {
        fireBaseService.getOrCreateChat(user)
    }

    override fun createGroupChat(listOfUsers: MutableList<com.envyglit.core.domain.entities.user.User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }

    override fun updateChat(chat: Chat) {
        fireBaseService.updateChat(chat)
    }

    override fun sendMessage(message: com.envyglit.core.domain.entities.message.BaseMessage, chatId: String) {
        fireBaseService.sendMessage(message, chatId)
    }

    override fun addMessagesListener(
        chatId: String,
        onListen: (List<com.envyglit.core.domain.entities.message.BaseMessage>) -> Unit
    ): ListenerRegistration {
        return fireBaseService.setChatMessagesListener(chatId, onListen)
    }
}
