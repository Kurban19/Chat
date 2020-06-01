package ru.skillbranch.chat.models

data class ChatChannel(val userIds: MutableList<String>) {
    constructor(): this(mutableListOf())
}