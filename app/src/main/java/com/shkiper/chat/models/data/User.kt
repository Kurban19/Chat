package com.shkiper.chat.models.data


import com.shkiper.chat.extensions.humanizeDiff
import com.shkiper.chat.utils.Utils
import java.util.*

data class User (
    var id:String = "",
    var firstName:String = "",
    var lastName:String = "",
    var avatar:String? = null,
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
            "${firstName.orEmpty()} ${lastName.orEmpty()}",
            Utils.toInitials(firstName, lastName)!!,
            avatar,
            lastActivity,
            false,
            isOnline
        )
    }


    companion object Factory {
        private var lastid : Int = -1

        fun makeUser(fullName:String) : User {
            lastid++
            val (firsName, lastName) = Utils.parseFullName(fullName)

            return User(
                id = "$lastid",
                firstName = firsName ?: "John",
                lastName = lastName ?: "Doe",
                email = ""
            )
        }
    }

}