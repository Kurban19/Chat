package com.envyglit.core.ui.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.envyglit.core.domain.entities.chat.Chat
import org.json.JSONObject

fun DocumentSnapshot.toChat(): Chat {

    val gson = GsonBuilder()
        .setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.name == "type"
            }

            override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                return false
            }
        }).create()

    val id = this["id"].toString()
    val title = this["title"].toString()
    val members = mutableListOf<com.envyglit.core.domain.entities.user.User>()
    val archived = this["archived"].toString() == "true"

    val jsonMembers = this["members"]?.let { jsonMember ->
        jsonMember as ArrayList<*>
        jsonMember.map { JSONObject(it as HashMap<*, *>) }
    }

    jsonMembers?.forEach {
        members.add(gson.fromJson(it.toString(), com.envyglit.core.domain.entities.user.User::class.java))
    }

    val jsonMessage = this.get("lastMessage")?.let {
        JSONObject(it as HashMap<*, *>)
    }


    val message = if(jsonMessage?.get("type")  == "text"){
        gson.fromJson(jsonMessage.toString(), com.envyglit.core.domain.entities.message.TextMessage::class.java)
    }
    else {
        gson.fromJson(jsonMessage.toString(), com.envyglit.core.domain.entities.message.ImageMessage::class.java)
    }

    return Chat(id, title, members, message, archived)
}