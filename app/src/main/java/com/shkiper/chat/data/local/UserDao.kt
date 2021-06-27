package com.shkiper.chat.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shkiper.chat.domain.entities.data.User
import io.reactivex.Observable

@Dao
interface UserDao {

    @Query("SELECT * FROM Users")
    fun getUsers(): Observable<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUsers(chats: List<User>)

}