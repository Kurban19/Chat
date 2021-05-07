package com.shkiper.chat.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.Gson
import com.shkiper.chat.model.ImageMessage
import com.shkiper.chat.model.TextMessage
import com.shkiper.chat.model.data.Chat
import org.json.JSONObject

fun DocumentSnapshot.toChat(): Chat {
    val id = this["id"].toString()
    val title = this["field"].toString()
    val members = listOf(this["members"].toString())
    val archived = this["archived"].toString() == "true"

    val jsonMessage = JSONObject(this.get("lastMessage") as HashMap<*, *>)

    val message = if(jsonMessage["type"] == "text"){
        Gson().fromJson(jsonMessage.toString(), TextMessage::class.java)
    }
    else {
        Gson().fromJson(jsonMessage.toString(), ImageMessage::class.java)
    }


    return Chat(id,title, members,message, archived)
}