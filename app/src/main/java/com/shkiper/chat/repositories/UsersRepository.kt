package com.shkiper.chat.repositories

import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.models.data.User

object UsersRepository {

    private val users = mutableLiveData(listOf<User>())

    fun loadUsers() = users

    fun addUser(user: User){
        val copy = users.value!!.toMutableList()
        copy.add(user)
        users.value = copy
    }


    fun findUser(userId: String): User?{
        users.value!!.forEach {
            if(userId == it.id){
                return it
            }
        }
        return null
    }

    fun findUsersById(ids: List<String>): List<User> {
        return users.value!!.filter { ids.contains(it.id) }
    }


}