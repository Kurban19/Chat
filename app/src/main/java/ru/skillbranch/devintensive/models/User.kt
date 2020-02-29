package ru.skillbranch.devintensive.models


import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User (
    var id:String,
    var firstName:String?,
    var lastName:String?,
    var avatar:String?,
    var rating:Int =  0,
    var respect:Int = 0,
    var lastVisit:Date? = null,
    var isOnline:Boolean = false
) {


    constructor(id: String, firstName: String?, lastName: String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )


    constructor(id: String) : this(id, "John", "Doe")

    init {

        println("It's Alive!!! \n" +
                "${if(lastName==="Doe") "His name id $firstName  $lastName" else "And his name is $firstName $lastName" }\n")
    }


    companion object Factory {
        private var lastid : Int = -1
        fun makeUser(fullName:String?) : User{
            lastid++

            var (firsName, lastName) = Utils.parseFullName(fullName)


            return User(id = "$lastid", firstName = firsName, lastName = lastName)
        }
    }


    class Builder{
        var newUser: User

        constructor(){
            newUser = User("2", "John", "Cena")
        }

        fun id(id: String): Builder{
            newUser.id = id
            return this
        }

        fun firstName(name: String): Builder{
            newUser.firstName = name
            return this
        }
        fun lastName(lastName: String): Builder{
            newUser.lastName = lastName
            return this
        }
        fun avatar(avatar: String): Builder{
            newUser.avatar = avatar
            return this
        }
        fun rating(rating: Int): Builder{
            newUser.rating = rating
            return this
        }
        fun respect(respect: Int): Builder{
            newUser.respect = respect
            return this
        }
        fun lastVisit(date: Date): Builder{
            newUser.lastVisit = date
            return this
        }
        fun isOnline(b: Boolean): Builder{
            newUser.isOnline = b
            return this
        }
        fun build(): User{
            return newUser
        }

    }
}