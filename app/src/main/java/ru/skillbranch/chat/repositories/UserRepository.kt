package ru.skillbranch.chat.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.data.managers.CacheManager
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User

object UserRepository {

    private val users = MutableLiveData<List<User>>()

    fun loadChats() : MutableLiveData<List<User>> {
        return users
    }

    fun addUser(user: User){
        val copy = users.value?.toMutableList() ?: mutableListOf()
        copy.add(user)
        users.value = copy
    }

    fun update(user: User) {
        val copy = users.value!!.toMutableList()
        val ind = users.value!!.indexOfFirst { it.id == user.id }
        if(ind == -1) return
        copy[ind] = user
        users.value = copy
    }

    fun find(userId: String): User? {
        val ind = users.value!!.indexOfFirst { it.id == userId}
        return users.value!!.getOrNull(ind)
    }
}