package com.envyglit.chat.domain.repository

import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.user.User
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.Observable

interface Repository {
    fun createChat(user: User)
    fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String)

    fun getUsers(): Observable<List<User>>
    fun findUserById(userId: String): User?
    fun findUsers(ids: List<String>): MutableList<User>?

    fun sendMessage(message: BaseMessage, chatId: String)
    fun addMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration
}