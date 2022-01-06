package com.envyglit.main.data

import androidx.lifecycle.MutableLiveData
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.main.domain.MainRepository
import io.reactivex.Observable

class MainRepositoryImpl(private val fireBaseService: FireBaseService): MainRepository {

    val chats = MutableLiveData<List<Chat>>(listOf())

    override fun getChats(): Observable<List<Chat>> {
        return fireBaseService.getEngagedChats()
            .doOnNext() {
                chats.value = it
            }
    }

    override fun findChatById(chatId: String): Chat {
        val ind = chats.value?.indexOfFirst { it.id == chatId } ?: 0
        val chat = chats.value?.get(ind)
        chat ?: throw KotlinNullPointerException("Chat not found")
        return chat
    }

    override fun updateChat(chat: Chat) {
        fireBaseService.updateChat(chat)
    }

}
