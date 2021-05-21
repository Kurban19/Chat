package com.shkiper.chat.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import io.reactivex.Observable

@Dao
interface UserDao {

    @Query("SELECT * FROM Users")
    fun getUsers(): Observable<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(chats: List<User>)

}