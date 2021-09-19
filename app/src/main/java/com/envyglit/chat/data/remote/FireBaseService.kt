package com.envyglit.chat.data.remote

import com.google.firebase.firestore.ListenerRegistration
import com.envyglit.chat.domain.entities.message.BaseMessage
import com.envyglit.chat.domain.entities.data.Chat
import com.envyglit.chat.domain.entities.user.User
import io.reactivex.Observable

interface FireBaseService {

    fun getUsers(): Observable<List<User>>

    fun getEngagedChats(): Observable<List<Chat>>

    fun getOrCreateChat(otherUser: User)

    fun createGroupChat(listOfUsers: MutableList<User>, titleOfChat: String)

    fun setChatMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration

    fun updateChat(chat: Chat)

    fun sendMessage(message: BaseMessage, chatId: String)

}