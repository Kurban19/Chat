package ru.skillbranch.chat.models

import ru.skillbranch.chat.extensions.humanizeDiff
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import java.util.*

class ImageMessage(
    id: String,
    from: User?,
    chat: Chat,
    isIncoming: Boolean = false,
    isRead: Boolean = false,
    date: Date = Date(),
    val type: String = "image",
    var image: String?

) : BaseMessage(id, from, chat, isIncoming, isRead, date){

    override fun formatMessage(): String = "id $id ${from?.firstName} " +
            "${if(isIncoming) "получил" else " отправил"} сообщение \"$image\" ${date.humanizeDiff()}"

}