package com.shkiper.chat.model.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Exclude
import com.shkiper.chat.App
import com.shkiper.chat.di.component.DaggerAppComponent
import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.ImageMessage
import com.shkiper.chat.model.TextMessage
import com.shkiper.chat.utils.FireBaseUtils
import com.shkiper.chat.utils.Utils
import java.util.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class Chat(
        val id: String = "",
        var title: String = "",
        val members: List<String> = listOf(),
        var lastMessage: BaseMessage? = null,
        var archived: Boolean = false){



    private fun unreadableMessageCount(): Int = FireBaseUtils.getUnreadMessages(id)

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

        val user = DaggerAppComponent.create()
                .getMainRepository()
                .findUser(members.find { FirebaseAuth.getInstance().currentUser.uid != it }!!)!!

//        val user = User()

        Log.d("Chat", user.toString())


        return if (isSingle()) {
            val chatItem = ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName),
                "${user.firstName} ${user.lastName}",
                lastMessageShort().first,
                unreadMessageCount(),
                lastMessageDate()?.shortFormat(),
                user.isOnline
            )
            chatItem
        }
        else {
            ChatItem(
                    id,
                null,
                    title[0].toString() ,
                    title,
                    lastMessageShort().first,
                    unreadMessageCount(),
                    lastMessageDate()?.shortFormat() ?: "",
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