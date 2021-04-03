package com.shkiper.chat.interfaces

import com.shkiper.chat.models.data.User

interface FireBaseUsers{

    fun getUsers(onListen: (List<User>) -> Unit)

}