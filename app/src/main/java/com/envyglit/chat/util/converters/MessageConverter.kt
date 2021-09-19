package com.envyglit.chat.util.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.envyglit.chat.domain.entities.message.BaseMessage
import com.envyglit.chat.domain.entities.message.ImageMessage
import com.envyglit.chat.domain.entities.message.TextMessage
import org.json.JSONObject

class MessageConverter {

    @TypeConverter
    fun fromMessage(message: BaseMessage): String {
        val gson = Gson()


        val type = if(message is TextMessage){
            object : TypeToken<List<TextMessage?>?>() {}.type
        }
        else{
            object : TypeToken<List<ImageMessage?>?>() {}.type
        }

        return gson.toJson(message, type)
    }




    @TypeConverter
    fun toMessage(data: String): BaseMessage? {
        val gson = Gson()

        val jsonMessage = JSONObject(data)

        val message = if(jsonMessage.get("type") == "text"){
            gson.fromJson(jsonMessage.toString(), TextMessage::class.java)
        }
        else {
            gson.fromJson(jsonMessage.toString(), ImageMessage::class.java)
        }


//        val type = object : TypeToken<List<User?>?>() {}.type
        return message
    }

}