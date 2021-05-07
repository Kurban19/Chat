package com.shkiper.chat.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.shkiper.chat.model.data.Chat


fun DocumentSnapshot.toChat(){
//    val chat = Chat(this["id"].toString(), this["title"].toString(), listOf(this["members"].toString()), null,)
    val id = this["id"].toString()
    val title = this["title"].toString()
    val members = listOf(this["members"].toString())
    val message = this["lastMessage"]
//    this.

}