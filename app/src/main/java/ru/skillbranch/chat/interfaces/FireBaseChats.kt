package ru.skillbranch.chat.interfaces

import com.google.firebase.firestore.ListenerRegistration
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User

interface FireBaseChats {

    fun createGroupChat(listOfUsers: MutableList<User>, titleOfChat: String)

    fun getOrCreateChat(otherUser: User)

    fun getEngagedChats()

    fun addChatMessagesListener(chatId: String, onListen: (List<TextMessage>) -> Unit): ListenerRegistration

    fun getUnreadMessages(chatId: String): Int

    fun updateChat(chat: Chat)

    fun sendMessage(message: TextMessage, chatId: String)


}