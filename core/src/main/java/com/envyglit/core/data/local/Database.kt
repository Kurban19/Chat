package com.envyglit.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.envyglit.core.data.local.converters.DateConverter
import com.envyglit.core.data.local.converters.MembersConverter
import com.envyglit.core.data.local.converters.MessageConverter
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.user.User

@TypeConverters(MembersConverter::class, DateConverter::class, MessageConverter::class)
@Database(entities = [User::class, Chat::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}