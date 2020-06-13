package ru.skillbranch.chat.data.managers

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.extensions.mutableLiveData
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User

object CacheManager {
    private val chats = mutableLiveData(listOf<Chat>())
    private var users = mutableLiveData(FireBaseUtil.getUsers())

    const val TAG = "CacheManager"

    fun loadChats(): MutableLiveData<List<Chat>> {
        return chats
    }

    fun loadUsers(): MutableList<User>? {
        return users.value
    }

    fun findUser(userId: String): User{
        users.value!!.forEach {
            if(userId == it.id){
                return it;
            }
        }
        return users.value!!.first()
    }

    fun findUsersById(ids: List<String>): List<User> {
        return users.value!!.filter { ids.contains(it.id) }
    }

    fun nextChatId(): String{
        return "${chats.value!!.size}"
    }

    fun insertChat(chat: Chat){
        val copy = chats.value!!.toMutableList()
        copy.add(chat)
        chats.value = copy
    }
}