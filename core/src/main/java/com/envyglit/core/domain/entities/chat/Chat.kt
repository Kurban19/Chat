package com.envyglit.core.domain.entities.chat

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.envyglit.core.ui.extensions.shortFormat
import com.envyglit.core.ui.utils.Utils
import com.envyglit.core.data.local.converters.MembersConverter
import com.envyglit.core.data.local.converters.MessageConverter
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.message.ImageMessage
import com.envyglit.core.domain.entities.message.TextMessage
import com.envyglit.core.domain.entities.user.User
import com.envyglit.core.ui.entities.chat.ChatItem
import java.util.*

@Entity(tableName = "Chats")
@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class Chat(
    @PrimaryKey
    val id: String = "",
    var title: String = "",
    @TypeConverters(MembersConverter::class)
    val members: List<User> = listOf(),
    @TypeConverters(MessageConverter::class)
    var lastMessage: BaseMessage? = null,
    var archived: Boolean = false,
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

    fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem { //TODO mapper to ui layer

        return if (isSingle()) {

//            val user = members.find {  FirebaseAuth.getInstance().currentUser!!.uid != it.id } ?: User()
            val user = User("3452354", "John", "Doe")

            ChatItem(
                id,
                avatar,
                Utils.toInitials(user.firstName, user.lastName),
                title = "${members[0].firstName} ${members[0].lastName}",
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