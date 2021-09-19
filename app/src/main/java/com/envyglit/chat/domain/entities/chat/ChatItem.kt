package com.envyglit.chat.domain.entities.chat

import com.envyglit.chat.domain.entities.data.ChatType

data class ChatItem (
    val id: String,
    val avatar: String?,
    val initials: String,
    val title: String,
    val shortDescription: String?,
    var messageCount: Int = 0,
    val lastMessageDate: String?,
    val isOnline: Boolean = false,
    val chatType: ChatType = ChatType.SINGLE,
    var author: String? = null,
    val archived: Boolean = false
)