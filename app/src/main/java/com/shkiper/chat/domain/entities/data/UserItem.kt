package com.shkiper.chat.domain.entities.data

data class UserItem (
    val id: String,
    val fullName: String,
    val initials: String,
    val avatar: String?,
    val lastActivity: String,
    val isSelected: Boolean = false,
    val isOnline: Boolean = false
)