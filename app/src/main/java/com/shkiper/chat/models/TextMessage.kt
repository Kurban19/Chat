package com.shkiper.chat.models


import com.shkiper.chat.extensions.humanizeDiff
import com.shkiper.chat.models.data.User


import java.util.*

class TextMessage(
    id: String = "",
    from: User = User(),
    isRead: Boolean = false,
    isIncoming: Boolean = false,
    date: Date = Date(),
    override val type: String = "text",
    var text: String = ""
) : BaseMessage(id, from, isIncoming, isRead, date, type){



    override fun formatMessage(): String = "id $id ${from.firstName} " +
            "${if(isIncoming) "получил" else " отправил"} сообщение \"$text\" ${date.humanizeDiff()}"


    companion object Factory {
        private var lastid : Int = -1
        fun makeMessage(text: String, from: User) : TextMessage {
            lastid++
            return TextMessage(
                    id = "$lastid",
                    from = from,
                    text = text
            )
        }
    }


}