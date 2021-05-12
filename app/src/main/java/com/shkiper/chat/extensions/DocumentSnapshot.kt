package com.shkiper.chat.extensions

import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.shkiper.chat.model.ImageMessage
import com.shkiper.chat.model.TextMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import org.json.JSONObject

//I know it's shitty function, I hope no one sees it
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
    val title = this["field"].toString()
    val members = mutableListOf<User>()
    val archived = this["archived"].toString() == "true"

    val jsonMembers = this["members"]?.let {
        it as ArrayList<*>
        it.map { JSONObject(it as HashMap<*, *>) }
    }

    jsonMembers?.forEach {
        members.add(gson.fromJson(it.toString(), User::class.java))
    }

    val jsonMessage = this.get("lastMessage")?.let {
        JSONObject(it as HashMap<*, *>)
    }

    val message = if(jsonMessage?.get("type")  == "text"){
        gson.fromJson(jsonMessage.toString(), TextMessage::class.java)
    }
    else {
        gson.fromJson(jsonMessage.toString(), ImageMessage::class.java)
    }


    return Chat(id, title, members, message, archived)
}