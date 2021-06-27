    package com.shkiper.chat.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.data.remote.FireBaseService
import com.shkiper.chat.domain.repository.Repository
import com.shkiper.chat.domain.entities.BaseMessage
import com.shkiper.chat.domain.entities.data.Chat
import com.shkiper.chat.domain.entities.data.User
import com.shkiper.chat.data.local.Database
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class RepositoryImpl(private val fireBaseService: FireBaseService, private val database: Database):
    Repository {

    val chats = MutableLiveData<List<Chat>>(listOf())
    val users = MutableLiveData<List<User>>(listOf())


    override fun getUsers(): Observable<List<User>> {
        return fireBaseService.getUsers()
    }


    override fun getEngagedChats(): Observable<List<Chat>> {
        return Observable.concat(
            getChatsFromDb(),
            getChatsFromApi())
    }


    private fun getChatsFromDb(): Observable<List<Chat>> {
        return database.chatDao().getChats().filter { it.isNotEmpty() }
    }

    private fun getChatsFromApi(): Observable<List<Chat>> {
        return fireBaseService.getEngagedChats()
            .doOnNext {
                storeChatsInDb(it)
            }
    }


    private fun storeChatsInDb(chats: List<Chat>) {
        database.chatDao().insertAllChats(chats)
    }



    fun findUserById(userId: String): User {
        return users.value!!.find { it.id == userId }!!
    }


    fun findChatById(chatId: String): Chat {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!![ind]
    }

    fun findUsers(ids: List<String>): MutableList<User> {
        return users.value!!.filter { ids.contains(it.id) }.toMutableList()
    }

    fun createChat(user: User){
        fireBaseService.getOrCreateChat(user)
    }


    fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }


    fun updateChat(chat: Chat) {
//        val copy = chats.value!!.toMutableList()
//        val index = chats.value!!.indexOfFirst { it.id == chat.id }
//        if (index == -1) return
//        copy[index] = chat
//        chats.value = copy
        fireBaseService.updateChat(chat)
    }


    fun sendMessage(message: BaseMessage, chatId: String){
        fireBaseService.sendMessage(message, chatId)
    }


    fun addMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration {
        return fireBaseService.setChatMessagesListener(chatId, onListen)
    }
}
