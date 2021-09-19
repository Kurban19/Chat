package com.envyglit.chat.domain.interactors

import com.envyglit.chat.domain.repository.Repository
import javax.inject.Inject

class UsersInteractor @Inject constructor(
    private val repository: Repository
) {

}
