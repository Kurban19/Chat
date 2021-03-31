package com.shkiper.chat.interfaces

import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.models.data.User

interface FireBaseChats {

    fun setEngagedChatsListener(onListen: (List<Chat>) -> Unit): ListenerRegistration

    fun getOrCreateChat(otherUser: User)

    fun createGroupChat(listOfUsers: MutableList<User>, titleOfChat: String)

    fun setChatMessagesListener(chatId: String, onListen: (List<TextMessage>) -> Unit): ListenerRegistration

    fun updateChat(chat: Chat)

    fun sendMessage(message: TextMessage, chatId: String)


}