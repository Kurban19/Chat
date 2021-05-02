package com.shkiper.chat.models

import com.shkiper.chat.models.data.User
import java.util.*

abstract class BaseMessage(
        val id: String = "",
        val from: User = User(),
        val isIncoming: Boolean = false,
        var isRead: Boolean = false,
        var group: Boolean = false,
        val date: Date = Date(),
        open val type: String = "text"
) {


    abstract fun formatMessage(): String

}