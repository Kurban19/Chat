package ru.skillbranch.chat.models.data


import ru.skillbranch.chat.extensions.humanizeDiff
import ru.skillbranch.chat.utils.Utils
import java.util.*

data class User (
    var id:String,
    var firstName:String,
    var lastName:String?,
    var avatar:String?,
    var lastVisit: Date? = null,
    var isOnline:Boolean = false,
    var email: String
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


    constructor(id: String, firstName: String, lastName: String, email: String) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        avatar = null
    )

    ;constructor(id: String, firstName: String, lastName: String, email: String, date: Date) : this(
            id = id,
            firstName = firstName,
            lastName = lastName,
            email = email,
            avatar = null,
            lastVisit = date)

    constructor(): this("42332253fd", "John", "Doe", "")

    constructor(id: String, firstName: String, email: String):this(id, firstName, "", email)

    constructor(id: String) : this(id, "John", "Doe", "")


    companion object Factory {
        private var lastid : Int = -1
        fun makeUser(fullName:String?) : User {
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