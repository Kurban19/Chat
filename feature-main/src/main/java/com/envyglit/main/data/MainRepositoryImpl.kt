package com.envyglit.main.data

import androidx.lifecycle.MutableLiveData
import com.envyglit.core.data.local.Database
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.main.domain.MainRepository
import io.reactivex.Observable

class MainRepositoryImpl(private val fireBaseService: FireBaseService, private val database: Database): MainRepository {

    val chats = MutableLiveData<List<Chat>>(listOf())

    override fun getChats(): Observable<List<Chat>> {
//        return Observable.concat(
//            getChatsFromDb(),
//            getChatsFromApi())
        return fetchChatsFromApi()
    }

    private fun getChatsFromDb(): Observable<List<Chat>> {
        return database.chatDao().getChats().filter { it.isNotEmpty() }
    }

    private fun fetchChatsFromApi(): Observable<List<Chat>> {
        return fireBaseService.getEngagedChats()
            .doOnNext() {
//                storeChatsInDb(it)
                chats.value = it
            }
    }

    private fun storeChatsInDb(chats: List<Chat>) {
        database.chatDao().insertAllChats(chats)
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