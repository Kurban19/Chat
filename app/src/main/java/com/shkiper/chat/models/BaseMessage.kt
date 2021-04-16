package com.shkiper.chat.models

import com.shkiper.chat.models.data.User
import java.util.*

abstract class BaseMessage(
        val id: String = "",
        val from: User = User(),
        val isIncoming: Boolean = false,
        var isRead: Boolean = false,
        var isGroup: Boolean = false,
        val date: Date = Date(),
        open val type: String = "text"
) {


    abstract fun formatMessage(): String
    companion object AbstractFactory{
        var lastId = -1
        fun makeMessage(from: User, date: Date = Date(), type: String = "text", payload: Any?, isIncoming: Boolean = false, isRead: Boolean = false, isGroup: Boolean = false) : BaseMessage{
            lastId++
            return when(type){
                "image" -> ImageMessage("$lastId", from, isIncoming = isIncoming, isRead = isRead, date = date, image = payload as String, type = "image", isGroup = isGroup)
                else -> TextMessage("$lastId", from, isIncoming = isIncoming, date = date, isRead = isRead, text = payload as String, type = type)
            }
        }
    }
}