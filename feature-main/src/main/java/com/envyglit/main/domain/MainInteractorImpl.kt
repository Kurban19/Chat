package com.envyglit.main.domain

import com.envyglit.core.domain.entities.chat.Chat
import io.reactivex.Observable
import javax.inject.Inject

class MainInteractorImpl @Inject constructor(repository: MainRepository): MainInteractor {

    override fun getChats(): Observable<List<Chat>> {
        TODO("Not yet implemented")
    }

    override fun findChatById(chatId: String): Chat {
        TODO("Not yet implemented")
    }

    override fun updateChat(chat: Chat) {
        TODO("Not yet implemented")
    }

}
