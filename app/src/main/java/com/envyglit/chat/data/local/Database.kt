package com.envyglit.chat.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.envyglit.chat.domain.entities.data.Chat
import com.envyglit.chat.domain.entities.data.User
import com.envyglit.chat.util.converters.DateConverter
import com.envyglit.chat.util.converters.MembersConverter
import com.envyglit.chat.util.converters.MessageConverter

@TypeConverters(MembersConverter::class, DateConverter::class, MessageConverter::class)
@Database(entities = [User::class, Chat::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}