package ru.skillbranch.chat.models


import ru.skillbranch.chat.extensions.humanizeDiff
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User


import java.util.*

class TextMessage(
    id: String,
    from: User?,
    chat: Chat,
    isIncoming: Boolean = false,
    date: Date = Date(),
    var text: String?
) : BaseMessage(id, from, chat, isIncoming, date){


    override fun formatMessage(): String = "id $id ${from?.firstName} " +
            "${if(isIncoming) "получил" else " отправил"} сообщение \"$text\" ${date.humanizeDiff()}"

}