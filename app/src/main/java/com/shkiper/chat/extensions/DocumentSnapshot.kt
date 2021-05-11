package com.shkiper.chat.extensions

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.shkiper.chat.model.ImageMessage
import com.shkiper.chat.model.TextMessage
import com.shkiper.chat.model.data.Chat
import org.json.JSONObject


fun DocumentSnapshot.toChat(): Chat {
    val id = this["id"].toString()
    val title = this["field"].toString()
    val members = listOf(this["members"].toString())
    val archived = this["archived"].toString() == "true"


    val jsonMessage = this.get("lastMessage")?.let {
        JSONObject(it as HashMap<*, *>)
    }

    Log.d("Tag", jsonMessage.toString())
    val gson = GsonBuilder()
            .setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes): Boolean {
                    return f.name == "type"
                }

                override fun shouldSkipClass(clazz: Class<*>?): Boolean {
                    return false
                }
            }).create()

    val message = if(jsonMessage?.get("type")  == "text"){
        gson.fromJson(jsonMessage.toString(), TextMessage::class.java)
    }
    else {
        gson.fromJson(jsonMessage.toString(), ImageMessage::class.java)
    }


    return Chat(id, title, emptyList(), message, archived)
}