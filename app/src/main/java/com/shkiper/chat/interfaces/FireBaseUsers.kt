package com.shkiper.chat.interfaces

import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.models.data.User
import java.util.*

interface FireBaseUsers{

    fun setUsersListener(onListen: (List<User>) -> Unit): ListenerRegistration


}