package com.envyglit.home.domain

import com.envyglit.core.domain.entities.chat.Chat
import io.reactivex.Observable

interface HomeRepository {

    fun getChats(): Observable<List<Chat>>

    fun findChatById(chatId: String): Chat

    fun updateChat(chat: Chat)

}
