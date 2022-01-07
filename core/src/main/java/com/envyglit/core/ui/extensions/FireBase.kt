package com.envyglit.core.ui.extensions

import com.envyglit.core.domain.entities.user.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toUser(): User { //TODO move to data layer
    return com.envyglit.core.domain.entities.user.User(
        id = this.uid,
        firstName = this.displayName ?: "None",
        email = this.email ?: "None"
    )
}