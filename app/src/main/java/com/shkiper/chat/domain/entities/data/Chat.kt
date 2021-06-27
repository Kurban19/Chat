package com.shkiper.chat.domain.entities.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.firebase.auth.FirebaseAuth
import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.domain.entities.BaseMessage
import com.shkiper.chat.domain.entities.ImageMessage
import com.shkiper.chat.domain.entities.TextMessage
import com.shkiper.chat.util.Utils
import com.shkiper.chat.util.converters.MembersConverter
import com.shkiper.chat.util.converters.MessageConverter
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
    var archived: Boolean = false){


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
                "archived" to archived)
    }


    fun lastMessageShort(): Pair<String, String> = when(val lastMessage = lastMessage){
        is TextMessage -> lastMessage.text to lastMessage.from.firstName
        is ImageMessage -> "${lastMessage.from.firstName} - отправил фото" to  lastMessage.from.firstName
        else -> "Сообщений еще нет" to "undefine"
    }

    fun isSingle(): Boolean = members.size == 2

    fun toChatItem(): ChatItem {

        return if (isSingle()) {

            val user = members.find {  FirebaseAuth.getInstance().currentUser!!.uid != it.id } ?: User()

            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName),
                title= "${user.firstName} ${user.lastName}",
                lastMessageShort().first,
                unreadMessageCount(),
                lastMessageDate = lastMessageDate()?.shortFormat() ?: "",
                user.isOnline,
                archived = archived
            )
        }
        else {
            ChatItem(
                id,
                avatar = null,
                title[0].toString() ,
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

enum class ChatType{
    SINGLE,
    GROUP,
    ARCHIVE
}