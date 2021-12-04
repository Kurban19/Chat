package com.envyglit.chat.domain.interactors

import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.domain.entities.chat.Chat
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
