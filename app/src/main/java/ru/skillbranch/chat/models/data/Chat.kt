package ru.skillbranch.chat.models.data

import ru.skillbranch.chat.firebase.FireBase
import ru.skillbranch.chat.extensions.shortFormat
import ru.skillbranch.chat.models.BaseMessage
import ru.skillbranch.chat.models.ImageMessage
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    var title: String,
    val members: List<User> = listOf(),
    var lastMessage: TextMessage? = null,
    var isArchived: Boolean = false){

    constructor() : this("", "Unknown")

    private fun unreadableMessageCount(): Int = FireBase.getUnreadMessages(id)

    private fun lastMessageDate(): Date {
        return lastMessage?.date ?: Date()
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
                "id" to id,
                "title" to title,
                "members" to members,
                "lastMessage" to lastMessage!!,
                "isArchived" to isArchived)
    }


    private fun lastMessageShort(): Pair<String, String> = when(val lastMessage = lastMessage){
        is TextMessage -> lastMessage.text to lastMessage.from.firstName
//        is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to  lastMessage.from.firstName
        else -> "Сообщений еще нет" to "undefine"
    }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        val user = members.first()
        return if (isSingle()) {
            ChatItem(
                    id,
                    user.avatar,
                    Utils.toInitials(user.firstName, user.lastName) ?: "??",
                    "${user.firstName ?: ""} ${user.lastName ?: ""}",
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate().shortFormat(),
                    user.isOnline
            )
        }
        else {
            ChatItem(
                    id,
                null,
                    title[0].toString(),
                    title,
                    lastMessageShort().first,
                    unreadableMessageCount(),
                    lastMessageDate().shortFormat(),
                false,
                    ChatType.GROUP,
                    lastMessageShort().second
            )
        }
    }
}

enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}