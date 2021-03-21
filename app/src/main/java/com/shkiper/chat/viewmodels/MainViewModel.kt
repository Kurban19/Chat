package com.shkiper.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.models.data.ChatItem
import com.shkiper.chat.repositories.ChatsRepository
import javax.inject.Inject

//@HiltViewModel
class MainViewModel @Inject constructor(
    private val chatsRepository: ChatsRepository
): ViewModel() {

    private val query = mutableLiveData("")
    private val chats = Transformations.map(chatsRepository.loadChats()) { chats ->
        return@map chats.filter{!it.isArchived}
            .map { it.toChatItem() }
    }


    fun getChatData() : LiveData<List<ChatItem>>{
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val users = chats.value!!

            result.value = if(queryStr.isEmpty()) users
            else users.filter { it.title.contains(queryStr,true) }
        }

        result.addSource(chats){filterF.invoke()}
        result.addSource(query){filterF.invoke()}

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatsRepository.find(chatId)
        chatsRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = chatsRepository.find(chatId)
        chatsRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

}