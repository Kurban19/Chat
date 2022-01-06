package com.envyglit.main.domain

import com.envyglit.core.domain.entities.chat.Chat
import io.reactivex.Observable

interface MainRepository {

    fun getChats(): Observable<List<Chat>>

    fun findChatById(chatId: String): Chat

    fun updateChat(chat: Chat)

}
