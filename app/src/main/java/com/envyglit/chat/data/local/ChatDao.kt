package com.envyglit.chat.data.local

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Observable
import androidx.room.Dao
import com.envyglit.core.domain.entities.chat.Chat

@Dao
interface ChatDao {

    @Query("SELECT * FROM Chats")
    fun getChats(): Observable<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: Chat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllChats(chats: List<Chat>)

}