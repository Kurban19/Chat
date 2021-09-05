package com.envyglit.chat.util.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.envyglit.chat.domain.entities.data.User


class MembersConverter {

    @TypeConverter
    fun fromMembers(users: List<User>): String {
        val gson = Gson()
        val type = object : TypeToken<List<User?>?>() {}.type
        return gson.toJson(users, type)

    }

    @TypeConverter
    fun toMembers(data: String): List<User> {
        val gson = Gson()
        val type = object : TypeToken<List<User?>?>() {}.type
        return gson.fromJson(data, type)
    }
}