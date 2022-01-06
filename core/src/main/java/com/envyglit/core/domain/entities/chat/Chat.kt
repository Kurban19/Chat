package com.envyglit.core.domain.entities.chat

import android.util.Log
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.message.ImageMessage
import com.envyglit.core.domain.entities.message.TextMessage
import com.envyglit.core.domain.entities.user.User
import com.envyglit.core.ui.entities.chat.ChatItem
import com.envyglit.core.ui.extensions.shortFormat
import com.envyglit.core.ui.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import java.util.*

data class Chat(
    val id: String = "",
    var title: String = "",
    val members: List<User> = listOf(),
    var lastMessage: BaseMessage? = null,
    var archived: Boolean = false,
    val single: Boolean = false,
    val avatar: String? = null
) {

//    private fun unreadableMessageCount(): Int = FireBaseUtils.getUnreadMessages(id)

    fun unreadMessageCount(): Int = 0

    fun lastMessageDate(): Date? {
        return lastMessage?.date
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "title" to title,
            "members" to members,
            "lastMessage" to lastMessage,
            "archived" to archived
        )
    }

    fun lastMessageShort(): Pair<String, String> = when (val lastMessage = lastMessage) {
        is TextMessage -> lastMessage.text to lastMessage.from.firstName
        is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to lastMessage.from.firstName
        else -> "Сообщений еще нет" to "undefine"
    }

    fun isSingle(): Boolean = single

    fun toChatItem(): ChatItem { //TODO mapper to ui layer

        return if (isSingle()) {

            val user = members.find {  FirebaseAuth.getInstance().currentUser?.uid != it.id } ?: User() //TODO delete FirebaseAuth

            Log.d("Logger", user.toString())

            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName),
                title = "${user.firstName} ${user.lastName}",
                lastMessageShort().first,
                unreadMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat() ?: "",
                user.isOnline,
                archived = archived
            )
        } else {
            ChatItem(
                id,
                avatar = null,
                title[0].toString(),
                title,
                lastMessageShort().first,
                unreadMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat() ?: "",
                isOnline = false,
                ChatType.GROUP,
                lastMessageShort().second,
                archived = archived
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}