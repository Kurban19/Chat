package ru.skillbranch.chat.models

import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import java.util.*

abstract class BaseMessage(
        val id: String,
        val from: User?,
        val chat: Chat?,
        val isIncoming: Boolean = false,
        var isRead: Boolean = false,
        val date: Date = Date(),
        open val type: String = "text"
) {
    abstract fun formatMessage(): String
    companion object AbstractFactory{
        var lastId = -1
        fun makeMessage(from: User?, chat: Chat, date: Date = Date(), type: String = "text", payload: Any?, isIncoming: Boolean = false, isRead: Boolean = false) : BaseMessage{
            lastId++
            return when(type){
                "image" -> ImageMessage("$lastId", from, chat, isIncoming = isIncoming, isRead = isRead, date = date, image = payload as String, type = "image")
                else -> TextMessage("$lastId", from, chat, isIncoming = isIncoming, date = date, isRead = isRead, text = payload as String, type = type)
            }
        }
    }
}