package ru.skillbranch.chat.models.data

import androidx.annotation.VisibleForTesting
import com.google.firebase.auth.FirebaseAuth
import ru.skillbranch.chat.extensions.shortFormat
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.BaseMessage
import ru.skillbranch.chat.models.ImageMessage
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<TextMessage> = mutableListOf(),
    var isArchived: Boolean = false){

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int = messages.filter { !it.isRead ; it.from!!.id != FirebaseAuth.getInstance().currentUser!!.toUser().id }.size
//    fun unreadableMessageCount(): Int = messages.filter { !it.isRead; it.from!!.id != FirebaseAuth.getInstance().currentUser!!.toUser().id }.filter { it.from!!.id != FirebaseAuth.getInstance().currentUser!!.toUser().id }.size


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        val message = messages.lastOrNull() ?: return Date()
        return message.date
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
                "id" to id,
                "title" to title,
                "members" to members,
                "messages" to messages,
                "isArchived" to isArchived)
    }

    constructor(): this(
            "lf32d3kf3nfl3234",
            "default",
            mutableListOf<User>(),
            mutableListOf<TextMessage>(),
            false)



    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String?, String> = when(val lastMessage = messages.lastOrNull()){
        is TextMessage -> lastMessage.text to  lastMessage.from!!.firstName!!
        //is ImageMessage -> "${lastMessage.from!!.firstName!!} - отправил фото" to  lastMessage.from.firstName!!
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
                lastMessageDate()?.shortFormat(),
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
                lastMessageDate()?.shortFormat(),
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