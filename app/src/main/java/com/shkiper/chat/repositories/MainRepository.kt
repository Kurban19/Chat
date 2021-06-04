package com.shkiper.chat.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.interfaces.FireBaseService
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import com.shkiper.chat.room.Database
import io.reactivex.Observable
import javax.inject.Singleton

@Singleton
class MainRepository(private val fireBaseService: FireBaseService, private val database: Database) {

    val chats = MutableLiveData<List<Chat>>(listOf())
    val users = MutableLiveData<List<User>>(listOf())


    fun getUsers(): Observable<List<User>> {
        return fireBaseService.getUsers()
    }

    fun getEngagedChats(): Observable<List<Chat>> {
        return fireBaseService.getEngagedChats()
            .doOnNext {
                chats.value = it
            }
    }


    fun addMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration {
        return fireBaseService.setChatMessagesListener(chatId, onListen)
    }


    fun createChat(user: User){
        fireBaseService.getOrCreateChat(user)
    }

    fun findUserById(userId: String): User{
//        users.value!!.forEach {
//            if(userId == it.id){
//                return it
//            }
//        }
//        return null
        return users.value!!.find { it.id == userId }!!
    }

    fun sendMessage(message: BaseMessage, chatId: String){
        fireBaseService.sendMessage(message, chatId)
    }


    fun findChatById(chatId: String): Chat {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!![ind]
    }


    fun findUsers(ids: List<String>): MutableList<User> {
        return users.value!!.filter { ids.contains(it.id) }.toMutableList()
    }


    fun updateChat(chat: Chat) {
//        val copy = chats.value!!.toMutableList()
//        val index = chats.value!!.indexOfFirst { it.id == chat.id }
//        if (index == -1) return
//        copy[index] = chat
//        chats.value = copy
        fireBaseService.updateChat(chat)
    }


    fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }

}
