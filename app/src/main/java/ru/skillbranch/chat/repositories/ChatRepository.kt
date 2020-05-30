package ru.skillbranch.chat.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.data.managers.CacheManager
import ru.skillbranch.chat.models.data.User

object ChatRepository {
    private val chats = CacheManager.loadChats()
    //private val chats = MutableLiveData<List<Chat>>()

    fun loadChats() : MutableLiveData<List<Chat>> {
        return chats
    }

    fun createChat(user: User){
        val title = "${user.firstName} ${user.lastName}"
        val chat = Chat(CacheManager.nextChatId(), title, listOf(user))
        val copy = chats.value!!.toMutableList()
        copy.add(chat)
        chats.value = copy
    }

    fun update(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        val ind = chats.value!!.indexOfFirst { it.id == chat.id }
        if(ind == -1) return
        copy[ind] = chat
        chats.value = copy
    }

    fun find(chatId: String): Chat? {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!!.getOrNull(ind)
    }
}
