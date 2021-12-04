package com.envyglit.chat.domain.repository

import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.user.User
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.Observable

interface Repository {
    fun getChats(): Observable<List<Chat>>
    fun findChatById(chatId: String): Chat?
    fun createChat(user: User)
    fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String)
    fun updateChat(chat: Chat)

    fun getUsers(): Observable<List<User>>
    fun findUserById(userId: String): User?
    fun findUsers(ids: List<String>): MutableList<User>?

    fun sendMessage(message: com.envyglit.core.domain.entities.message.BaseMessage, chatId: String)
    fun addMessagesListener(chatId: String, onListen: (List<com.envyglit.core.domain.entities.message.BaseMessage>) -> Unit): ListenerRegistration
}