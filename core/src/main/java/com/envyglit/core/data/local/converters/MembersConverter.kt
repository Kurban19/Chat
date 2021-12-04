package com.envyglit.core.data.local.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson


class MembersConverter {

    @TypeConverter
    fun fromMembers(users: List<com.envyglit.core.domain.entities.user.User>): String {
        val gson = Gson()
        val type = object : TypeToken<List<com.envyglit.core.domain.entities.user.User?>?>() {}.type
        return gson.toJson(users, type)

    }

    @TypeConverter
    fun toMembers(data: String): List<com.envyglit.core.domain.entities.user.User> {
        val gson = Gson()
        val type = object : TypeToken<List<com.envyglit.core.domain.entities.user.User?>?>() {}.type
        return gson.fromJson(data, type)
    }
}