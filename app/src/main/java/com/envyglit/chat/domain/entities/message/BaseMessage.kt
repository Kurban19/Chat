package com.envyglit.chat.domain.entities.message

import com.envyglit.chat.domain.entities.user.User
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