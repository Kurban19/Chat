package ru.skillbranch.chat.repositories

import ru.skillbranch.chat.data.managers.CacheManager
import ru.skillbranch.chat.extensions.replaceGarbage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.models.data.UserItem

object GroupRepository {

    fun loadUsers(): List<User> = CacheManager.loadUsers()!!

    fun createChat(items: List<UserItem>){
        val ids = items.map{it.id}
        val users = CacheManager.findUsersById(ids)
        val title = users.map { it.firstName }.toString().replaceGarbage()
        val chat = Chat(CacheManager.nextChatId(), title, users)
        CacheManager.insertChat(chat)
    }

}