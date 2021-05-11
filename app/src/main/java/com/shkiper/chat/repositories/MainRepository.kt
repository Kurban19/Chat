package com.shkiper.chat.repositories

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.interfaces.FireBaseService
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import javax.inject.Singleton

@Singleton
class MainRepository(private val fireBaseService: FireBaseService) {

    val chats = MutableLiveData<List<Chat>>(listOf())
    val users = MutableLiveData<List<User>>(listOf())

    init {
        fireBaseService.getEngagedChats(this::setChats)
        fireBaseService.getUsers(this::setUsers)
    }

    fun addMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration {
        return fireBaseService.setChatMessagesListener(chatId, onListen)
    }


    fun createChat(user: User){
        fireBaseService.getOrCreateChat(user)
    }

    fun findUser(userId: String?): User?{
        users.value!!.forEach {
            if(userId == it.id){
                return it
            }
        }
        return null
    }

    fun sendMessage(message: BaseMessage, chatId: String){
        fireBaseService.sendMessage(message, chatId)
    }


    fun findChat(chatId: String): Chat {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!![ind]
    }


    fun findUsers(ids: List<String>): MutableList<User> {
        return users.value!!.filter { ids.contains(it.id) }.toMutableList()
    }


    fun update(chat: Chat) {
        fireBaseService.updateChat(chat)
        updateData()
    }


    fun addToArchive(chat: Chat){
        val copy = chats.value!!.toMutableList()
        val index = chats.value!!.indexOfFirst { it.id == chat.id }
        if (index == -1) return
        copy[index] = chat
        chats.value = copy
        update(chat)
    }


    fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }

    private fun setUsers(listOfUsers: List<User>){
        users.value = listOfUsers
    }

    private fun setChats(listOfChats: List<Chat>){
        chats.value = listOfChats
    }


    fun updateData() {
        fireBaseService.getEngagedChats(this::setChats)
        fireBaseService.getUsers(this::setUsers)
    }
}
