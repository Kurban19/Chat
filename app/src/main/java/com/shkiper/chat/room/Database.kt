package com.shkiper.chat.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import com.shkiper.chat.util.converters.DateConverter
import com.shkiper.chat.util.converters.MembersConverter

@TypeConverters(MembersConverter::class, DateConverter::class)
@Database(entities = [User::class, Chat::class], version = 1, exportSchema = false)
abstract class Database : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}