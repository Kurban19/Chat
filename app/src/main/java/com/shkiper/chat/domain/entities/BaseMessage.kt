package com.shkiper.chat.domain.entities

import com.shkiper.chat.domain.entities.data.User
import java.util.*

abstract class BaseMessage(
    val id: String = "",
    val from: User = User(),
    val isIncoming: Boolean = false,
    var read: Boolean = false,
    var group: Boolean = false,
    val date: Date = Date(),
    open val type: String = "text"
) {


    abstract fun formatMessage(): String

}