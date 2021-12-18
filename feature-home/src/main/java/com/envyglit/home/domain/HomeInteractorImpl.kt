package com.envyglit.home.domain

import com.envyglit.core.domain.entities.chat.Chat
import io.reactivex.Observable
import javax.inject.Inject

class HomeInteractorImpl @Inject constructor(repository: HomeRepository): HomeInteractor {

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
