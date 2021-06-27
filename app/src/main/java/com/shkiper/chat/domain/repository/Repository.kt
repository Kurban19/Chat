package com.shkiper.chat.domain.repository

import com.shkiper.chat.domain.entities.data.Chat
import com.shkiper.chat.domain.entities.data.User
import io.reactivex.Observable

interface Repository {
    fun getEngagedChats(): Observable<List<Chat>>
    fun getUsers(): Observable<List<User>>
}