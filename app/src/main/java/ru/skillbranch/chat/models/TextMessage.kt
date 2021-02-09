package ru.skillbranch.chat.models


import ru.skillbranch.chat.extensions.humanizeDiff
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User


import java.util.*

class TextMessage(
    id: String,
    from: User,
    isRead: Boolean = false,
    isIncoming: Boolean = false,
    date: Date = Date(),
    override val type: String = "text",
    var text: String
) : BaseMessage(id, from, isIncoming, isRead, date, type){


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


    override fun formatMessage(): String = "id $id ${from?.firstName} " +
            "${if(isIncoming) "получил" else " отправил"} сообщение \"$text\" ${date.humanizeDiff()}"

}