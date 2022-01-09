package com.envyglit.core.data.remote

import com.google.firebase.firestore.ListenerRegistration
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.user.User
import io.reactivex.Observable

interface FireBaseService {

    fun getUsers(): Observable<List<User>>

    fun getEngagedChats(): Observable<List<Chat>>

    fun getOrCreateChat(otherUser: User)

    fun createGroupChat(listOfUsers: List<User>, titleOfChat: String)

    fun setChatMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration

    fun updateChat(chat: Chat)

    fun sendMessage(message: BaseMessage, chatId: String)

}