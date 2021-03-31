package com.shkiper.chat.models.data

import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.utils.FireBaseUtils
import com.shkiper.chat.utils.Utils
import java.util.*

data class Chat(
        val id: String = "",
        var title: String = "",
        val members: List<User> = listOf(),
        var lastMessage: TextMessage? = null,
        var isArchived: Boolean = false){


    private fun unreadableMessageCount(): Int = FireBaseUtils.getUnreadMessages(id)

    private fun unreadMessageCount(): Int = 0

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

    fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        val user = members.first()

        return if (isSingle()) {
            ChatItem(
                    id,
                    user.avatar,
                    Utils.toInitials(user.firstName, user.lastName) ?: "??",
                    "${user.firstName} ${user.lastName}",
                    lastMessageShort().first,
                    unreadMessageCount(),
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
                    unreadMessageCount(),
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