package ru.skillbranch.chat.data.managers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.extensions.mutableLiveData
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.ui.main.MainActivity

//import ru.skillbranch.chat.utils.DataGenerator

object CacheManager {
    private val chats = mutableLiveData(listOf<Chat>())
    //private val chats = mutableLiveData(FireBaseUtil.getChats())
    private var users = mutableLiveData(FireBaseUtil.getUsers())
    //private var users = mutableLiveData(mutableListOf<User>())

    const val TAG = "CacheManager"

    fun loadChats(): MutableLiveData<List<Chat>> {
        Log.d(TAG, users.value!!.size.toString())
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