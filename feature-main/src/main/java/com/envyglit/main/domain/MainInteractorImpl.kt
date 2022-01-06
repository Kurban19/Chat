package com.envyglit.main.domain

import com.envyglit.core.domain.entities.chat.Chat
import io.reactivex.Observable
import javax.inject.Inject

class MainInteractorImpl @Inject constructor(private val repository: MainRepository): MainInteractor {

    override fun getChats(): Observable<List<Chat>> {
        return repository.getChats()
    }

    override fun findChatById(chatId: String): Chat {
        return repository.findChatById(chatId)
    }

    override fun updateChat(chat: Chat) {
        repository.updateChat(chat)
    }

}
