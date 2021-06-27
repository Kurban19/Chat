package com.shkiper.chat.domain.repository

import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.domain.entities.BaseMessage
import com.shkiper.chat.domain.entities.data.Chat
import com.shkiper.chat.domain.entities.data.User
import io.reactivex.Observable

interface Repository {
    fun getEngagedChats(): Observable<List<Chat>>
    fun findChatById(chatId: String): Chat
    fun createChat(user: User)
    fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String)
    fun updateChat(chat: Chat)

    fun getUsers(): Observable<List<User>>
    fun findUserById(userId: String): User
    fun findUsers(ids: List<String>): MutableList<User>

    fun sendMessage(message: BaseMessage, chatId: String)
    fun addMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration
}