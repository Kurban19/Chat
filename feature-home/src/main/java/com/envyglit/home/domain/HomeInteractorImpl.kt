package com.envyglit.home.domain

import com.envyglit.core.domain.entities.chat.Chat
import io.reactivex.Observable

class HomeInteractorImpl(): HomeInteractor {
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
