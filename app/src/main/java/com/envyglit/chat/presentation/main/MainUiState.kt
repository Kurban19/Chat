package com.envyglit.chat.presentation.main

import com.envyglit.chat.domain.entities.data.ChatItem

data class MainUiState(
    val chatItems: List<ChatItem> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)
