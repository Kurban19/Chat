package com.envyglit.core.data.local.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import org.json.JSONObject

class MessageConverter {

    @TypeConverter
    fun fromMessage(message: com.envyglit.core.domain.entities.message.BaseMessage): String {
        val gson = Gson()


        val type = if(message is com.envyglit.core.domain.entities.message.TextMessage){
            object : TypeToken<List<com.envyglit.core.domain.entities.message.TextMessage?>?>() {}.type
        }
        else{
            object : TypeToken<List<com.envyglit.core.domain.entities.message.ImageMessage?>?>() {}.type
        }

        return gson.toJson(message, type)
    }




    @TypeConverter
    fun toMessage(data: String): com.envyglit.core.domain.entities.message.BaseMessage? {
        val gson = Gson()

        val jsonMessage = JSONObject(data)

        val message = if(jsonMessage.get("type") == "text"){
            gson.fromJson(jsonMessage.toString(), com.envyglit.core.domain.entities.message.TextMessage::class.java)
        }
        else {
            gson.fromJson(jsonMessage.toString(), com.envyglit.core.domain.entities.message.ImageMessage::class.java)
        }


//        val type = object : TypeToken<List<User?>?>() {}.type
        return message
    }

}