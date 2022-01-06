package com.envyglit.core.domain.entities.user

import androidx.room.Entity
import com.envyglit.core.ui.entities.user.UserItem
import com.envyglit.core.ui.extensions.humanizeDiff
import com.envyglit.core.ui.utils.Utils
import java.util.*

@Entity(tableName = "Users")
data class User (
    val id:String = "",
    val firstName:String = "",
    val lastName:String = "",
    val avatar:String? = null,
    val lastVisit: Date? = null,
    val isOnline:Boolean = false,
    val email: String = ""
) {

    fun toUserItem(): UserItem {
        val lastActivity = when{
            lastVisit == null -> "Еще ниразу не заходил"
            isOnline -> "online"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
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
