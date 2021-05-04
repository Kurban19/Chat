package com.shkiper.chat.interfaces

import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.models.BaseMessage
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.models.data.User

interface FireBaseService {

    fun getUsers(onListen: (List<User>) -> Unit)

    fun getEngagedChats(onListen: (List<Chat>) -> Unit)

    fun getOrCreateChat(otherUserId: String)

    fun createGroupChat(listOfUsersIds: MutableList<String>, titleOfChat: String)

    fun setChatMessagesListener(chatId: String, onListen: (List<BaseMessage>) -> Unit): ListenerRegistration

    fun updateChat(chat: Chat)

    fun sendMessage(message: BaseMessage, chatId: String)


}