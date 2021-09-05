package com.envyglit.chat.domain.entities.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.envyglit.chat.util.extensions.humanizeDiff
import com.envyglit.chat.util.Utils
import com.envyglit.chat.util.converters.DateConverter
import java.util.*

@Entity(tableName = "Users")
data class User (
    @PrimaryKey
    var id:String = "",
    var firstName:String = "",
    var lastName:String = "",
    var avatar:String? = null,
    @TypeConverters(DateConverter::class)
    var lastVisit: Date? = null,
    var isOnline:Boolean = false,
    var email: String = ""
) {


    fun toUserItem(): UserItem {
        val lastActivity = when{
            lastVisit == null -> "Еще ниразу не заходил"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit!!.humanizeDiff()}"
        }

        return UserItem(
            id,
            "$firstName $lastName",
            Utils.toInitials(firstName, lastName),
            avatar,
            lastActivity,
            false,
            isOnline
        )
    }


}