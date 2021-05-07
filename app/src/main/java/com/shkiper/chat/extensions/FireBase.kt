package com.shkiper.chat.extensions

import com.google.firebase.auth.FirebaseUser
import com.shkiper.chat.model.data.User

fun FirebaseUser.toUser(): User{
    return User(
            id = this.uid,
            firstName = this.displayName ?: "None",
            email = this.email ?: "None")
}