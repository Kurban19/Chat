package com.shkiper.chat.interfaces

import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import io.reactivex.Observable

interface FireBaseService {

    fun getUsers(): Observable<List<User>>

    fun getEngagedChats(onListen: (List<Chat>) -> Unit)

    fun getEngagedChatsRx(): Observable<List<Chat>>

    fun getOrCreateChat(otherUser: User)

    fun createGroupChat(listOfUsersIds: MutableList<User>, titleOfChat: String)

    fun setChatMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration

    fun updateChat(chat: Chat)

    fun sendMessage(message: BaseMessage, chatId: String)


}