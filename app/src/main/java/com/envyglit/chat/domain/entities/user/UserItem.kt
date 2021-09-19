package com.envyglit.chat.domain.entities.user

data class UserItem (
    val id: String,
    val fullName: String,
    val initials: String,
    val avatar: String?,
    val lastActivity: String,
    val isSelected: Boolean = false,
    val isOnline: Boolean = false
)
