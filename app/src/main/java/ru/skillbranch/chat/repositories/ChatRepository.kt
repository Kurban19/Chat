package ru.skillbranch.chat.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.data.managers.CacheManager

object ChatRepository {
    private val chats = CacheManager.loadChats()

    fun loadChats() : MutableLiveData<List<Chat>> {
        return chats
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
