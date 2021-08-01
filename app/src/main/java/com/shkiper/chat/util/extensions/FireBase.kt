package com.shkiper.chat.util.extensions

import com.google.firebase.auth.FirebaseUser
import com.shkiper.chat.domain.entities.data.User

fun FirebaseUser.toUser(): User {
    return User(
            id = this.uid,
            firstName = this.displayName ?: "None",
            email = this.email ?: "None")
}