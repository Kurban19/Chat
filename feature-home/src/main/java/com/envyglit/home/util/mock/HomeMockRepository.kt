package com.envyglit.home.util.mock

import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.utils.DataGenerator
import com.envyglit.home.domain.HomeRepository
import io.reactivex.Observable

class HomeMockRepository: HomeRepository {

    override fun getChats(): Observable<List<Chat>> {
        return Observable.create{ DataGenerator.stabChats}
    }

    override fun findChatById(chatId: String): Chat {
        return DataGenerator.stabChats.find { it.id == chatId }!!
    }

    override fun updateChat(chat: Chat) {
        TODO("Not yet implemented")
    }

}