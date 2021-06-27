package com.shkiper.chat.data.remote

import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.domain.entities.BaseMessage
import com.shkiper.chat.domain.entities.data.Chat
import com.shkiper.chat.domain.entities.data.User
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