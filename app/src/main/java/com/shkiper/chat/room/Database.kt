package com.shkiper.chat.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User


@Database(entities = [User::class, Chat::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun userDao(): UserDao
}