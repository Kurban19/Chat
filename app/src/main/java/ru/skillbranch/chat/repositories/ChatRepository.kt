package ru.skillbranch.chat.repositories

import androidx.lifecycle.MutableLiveData
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.extensions.mutableLiveData
import ru.skillbranch.chat.firebase.FireBaseChatsImpl
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatRepository @Inject constructor(fireBaseService: FireBaseChatsImpl) {

    private val chats = mutableLiveData(listOf<Chat>())

    init {
        fireBaseService.setEngagedChatsListener(this::setChat)
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


    private fun setChat(listOfChats: List<Chat>){
        chats.value = listOfChats
    }


    fun find(chatId: String): Chat {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!![ind]
    }


}
