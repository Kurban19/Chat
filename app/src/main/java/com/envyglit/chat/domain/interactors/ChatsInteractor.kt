package com.envyglit.chat.domain.interactors

import android.util.Log
import com.envyglit.chat.domain.entities.data.Chat
import com.envyglit.chat.domain.repository.Repository
import io.reactivex.Observable
import javax.inject.Inject

class ChatsInteractor @Inject constructor(
    private val repository: Repository
) {

    fun getChats(): Observable<List<Chat>> {
        return repository.getChats()
    }

    fun findChatById(chatId: String): Chat {
        return repository.findChatById(chatId)!!
    }

    fun updateChat(chat: Chat) {
        repository.updateChat(chat)
    }

}
