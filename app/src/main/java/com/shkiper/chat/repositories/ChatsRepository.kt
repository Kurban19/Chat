package com.shkiper.chat.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.firebase.FireBaseChatsImpl
import com.shkiper.chat.models.TextMessage
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatsRepository @Inject constructor(fireBaseService: FireBaseChatsImpl) {

    private val chats = mutableLiveData(listOf<Chat>())

    init {
        fireBaseService.addChatMessagesListener("7BYNqUMf5s9BSHY9iscb", this::setMessages)
        fireBaseService.setEngagedChatsListener(this::setChats)
    }

    fun loadChats() : MutableLiveData<List<Chat>> {
        return chats
    }

    fun setMessages(list: List<TextMessage>){

    }

    fun update(chat: Chat) {
        val copy = chats.value!!.toMutableList()
        val ind = chats.value!!.indexOfFirst { it.id == chat.id }
        if(ind == -1) return
        copy[ind] = chat
        chats.value = copy
    }


    private fun setChats(listOfChats: List<Chat>){
        Log.d("ChatsRepository", listOfChats.toString())
        chats.value = listOfChats
    }


    fun find(chatId: String): Chat {
        val ind = chats.value!!.indexOfFirst { it.id == chatId}
        return chats.value!![ind]
    }


}
