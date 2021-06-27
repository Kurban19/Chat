package com.shkiper.chat.domain.interactors

import com.shkiper.chat.domain.repository.Repository
import javax.inject.Inject

class MessagesInteractor @Inject constructor(
    private val repository: Repository
) {
}