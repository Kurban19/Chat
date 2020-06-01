package ru.skillbranch.chat.extensions

import com.google.firebase.auth.FirebaseUser
import ru.skillbranch.chat.models.data.User

fun FirebaseUser.toUser(): User{
    return User(
            id = this.uid,
            firstName = this.displayName ?: "None",
            email = this.email ?: "None")
}