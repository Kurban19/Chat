package com.shkiper.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.model.data.ChatItem
import com.shkiper.chat.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ArchiveViewModel @Inject constructor(
        private val mainRepository: MainRepository
        ): ViewModel() {
    private val query = mutableLiveData("")
    private val chats = Transformations.map(mainRepository.chats) { chats ->
        return@map chats.filter{it.archived}
            .map { it.toChatItem() }
    }

    fun getChats() : LiveData<List<ChatItem>>{
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
        val chat = mainRepository.findChat(chatId)
        mainRepository.updateChat(chat.copy(archived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = mainRepository.findChat(chatId)
        mainRepository.updateChat(chat.copy(archived = false))
    }

}