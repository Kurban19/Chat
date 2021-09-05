package com.envyglit.chat.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.envyglit.chat.data.remote.FireBaseService
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.chat.domain.entities.BaseMessage
import com.envyglit.chat.domain.entities.data.Chat
import com.envyglit.chat.domain.entities.data.User
import com.envyglit.chat.data.local.Database
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val fireBaseService: FireBaseService, private val database: Database) :
    Repository {

    val chats = MutableLiveData<List<Chat>>(listOf())
    val users = MutableLiveData<List<User>>(listOf())

    override fun getUsers(): Observable<List<User>> {
        return fireBaseService.getUsers()
            .doOnNext {
                users.value = it
            }
    }

    override fun getEngagedChats(): Observable<List<Chat>> {
//        return Observable.concat(
//            getChatsFromDb(),
//            getChatsFromApi())
        return getChatsFromApi()
    }

    private fun getChatsFromDb(): Observable<List<Chat>> {
        return database.chatDao().getChats().filter { it.isNotEmpty() }
    }

    private fun getChatsFromApi(): Observable<List<Chat>> {
        return fireBaseService.getEngagedChats()
            .doOnNext() {
//                storeChatsInDb(it)
                chats.value = it
            }
    }

    private fun storeChatsInDb(chats: List<Chat>) {
        database.chatDao().insertAllChats(chats)
    }

    override fun findUserById(userId: String): User {
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

    override fun findUsers(ids: List<String>): MutableList<User> {
        return users.value!!.filter { ids.contains(it.id) }.toMutableList()
    }

    override fun createChat(user: User) {
        fireBaseService.getOrCreateChat(user)
    }

    override fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }

    override fun updateChat(chat: Chat) {
        fireBaseService.updateChat(chat)
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
