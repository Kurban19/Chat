package com.envyglit.core.domain.entities.message


import com.envyglit.core.ui.extensions.humanizeDiff
import com.envyglit.core.domain.entities.user.User
import java.util.*

class TextMessage(
    id: String = "",
    from: User = User(), //TODO set from to nullable
    isRead: Boolean = false,
    isIncoming: Boolean = false,
    group: Boolean = false,
    date: Date = Date(),
    override val type: String = "text",
    var text: String = ""
) : com.envyglit.core.domain.entities.message.BaseMessage(id, from, isIncoming, isRead, group, date, type){


    override fun formatMessage(): String = "id $id ${from.firstName} " +
            "${if(isIncoming) "получил" else " отправил"} сообщение \"$text\" ${date.humanizeDiff()}"


    companion object Factory {
        private var lastid : Int = -1
        fun makeMessage(text: String, from: User) : TextMessage {
            lastid++
            return TextMessage(
                    id = "$lastid",
                    from = from,
                    text = text,
            )
        }
    }
}