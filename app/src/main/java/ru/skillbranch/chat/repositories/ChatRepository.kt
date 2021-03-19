package ru.skillbranch.chat.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.extensions.mutableLiveData
import ru.skillbranch.chat.firebase.FireBaseChatsImpl

object ChatRepository {

    private val chats = mutableLiveData(listOf<Chat>())


    init {
        FireBaseChatsImpl.getEngagedChats()
    }

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

    fun updateChat(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        val ind = chats.value!!.indexOfFirst { it.id == chat.id }
        if(ind == -1) return
        copy[ind] = chat
        chats.value = copy
    }

    fun insertChat(chat: Chat){
        val copy = chats.value!!.toMutableList()
        copy.add(chat)
        chats.value = copy
    }


    fun find(chatId: String): Chat {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!![ind]
    }


    fun haveChat(chatId: String): Boolean{
        chats.value!!.forEach {
            if(chatId == it.id){
                return true;
            }
        }
        return false
    }
}
