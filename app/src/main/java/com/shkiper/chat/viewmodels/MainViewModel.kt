package com.shkiper.chat.viewmodels

import androidx.lifecycle.*
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.ChatItem
import com.shkiper.chat.model.data.ChatType
import com.shkiper.chat.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
    ): ViewModel() {
    private val query = mutableLiveData("")
    private val chats = Transformations.map(mainRepository.chats) { chats ->
        val archived = chats.filter { it.archived }
        if (archived.isEmpty()) {
            return@map chats.map { it.toChatItem() }
        } else {
            val listWithArchive = mutableListOf<ChatItem>()
            listWithArchive.add(0, makeArchiveItem(archived))
            listWithArchive.addAll((chats.filter { !it.archived }.map { it.toChatItem() }))
            return@map listWithArchive
        }
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
        val chat = mainRepository.findChat(chatId)
        mainRepository.addToArchive(chat.copy(archived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = mainRepository.findChat(chatId)
        mainRepository.update(chat.copy(archived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }


    private fun makeArchiveItem(archived: List<Chat>): ChatItem {
        val count = archived.fold(0) { acc, chat -> acc + chat.unreadMessageCount() }

        val lastChat: Chat =
                if (archived.none { it.unreadMessageCount() != 0 }) archived.last() else
                    archived.filter { it.unreadMessageCount() != 0 }
                        .maxByOrNull { it.lastMessageDate()!! }!!

        return ChatItem(
                "-1",
                null,
                "",
                "Архив чатов",
                lastChat.lastMessageShort().first,
                count,
                lastChat.lastMessageDate()?.shortFormat(),
                false,
                ChatType.ARCHIVE,
                lastChat.lastMessageShort().second
        )
    }


}