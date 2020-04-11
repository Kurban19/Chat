package ru.skillbranch.devintensive.models.data


import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User (
    var id:String,
    var firstName:String?,
    var lastName:String?,
    var avatar:String?,
    var rating:Int =  0,
    var respect:Int = 0,
    var lastVisit: Date? = null,
    var isOnline:Boolean = false
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


    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )


    constructor(id: String) : this(id, "John", "Doe")

    companion object Factory {
        private var lastid : Int = -1
        fun makeUser(fullName:String?) : User {
            lastid++

            val (firsName, lastName) = Utils.parseFullName(fullName)

            return User(
                id = "$lastid",
                firstName = firsName,
                lastName = lastName
            )
        }
    }


    class Builder{
        private var newUser: User = User("2", "John", "Cena")

        constructor()

        fun id(id: String): Builder {
            newUser.id = id
            return this
        }

        fun firstName(name: String): Builder {
            newUser.firstName = name
            return this
        }
        fun lastName(lastName: String): Builder {
            newUser.lastName = lastName
            return this
        }
        fun avatar(avatar: String): Builder {
            newUser.avatar = avatar
            return this
        }
        fun rating(rating: Int): Builder {
            newUser.rating = rating
            return this
        }
        fun respect(respect: Int): Builder {
            newUser.respect = respect
            return this
        }
        fun lastVisit(date: Date): Builder {
            newUser.lastVisit = date
            return this
        }
        fun isOnline(b: Boolean): Builder {
            newUser.isOnline = b
            return this
        }
        fun build(): User {
            return newUser
        }

    }
}