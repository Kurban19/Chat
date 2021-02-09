package ru.skillbranch.chat.repositories

import ru.skillbranch.chat.data.managers.CacheManager
import ru.skillbranch.chat.extensions.mutableLiveData
import ru.skillbranch.chat.extensions.replaceGarbage
import ru.skillbranch.chat.firebase.FireBaseUtil
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.models.data.UserItem

object UsersRepository {

    private val users = mutableLiveData(FireBaseUtil.getUsers())


    fun loadUsers() = users


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

    fun createChat(items: List<UserItem>){
        val ids = items.map{it.id}
        val users = CacheManager.findUsersById(ids)
        val title = users.map { it.firstName }.toString().replaceGarbage()
        val chat = Chat(CacheManager.nextChatId(), title, users)
        CacheManager.insertChat(chat)
    }

}