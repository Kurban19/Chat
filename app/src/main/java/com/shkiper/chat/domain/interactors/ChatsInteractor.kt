package com.shkiper.chat.domain.interactors

import com.shkiper.chat.domain.entities.data.Chat
import com.shkiper.chat.domain.repository.Repository
import javax.inject.Inject

class ChatsInteractor @Inject constructor(
    private val repository: Repository
) {

    fun getChats() = repository.getEngagedChats()

    fun findChatById(chatId: String): Chat {
        return repository.findChatById(chatId)
    }

    fun updateChat(chat: Chat) {
        repository.updateChat(chat)
    }

}