package com.envyglit.users.domain

import com.envyglit.core.domain.entities.user.User
import io.reactivex.Observable

interface UsersRepository {

    fun getUsers(): Observable<List<User>>

    fun findUsers(userIds: List<String>): List<User>

    fun findUserById(userId: String): User

    fun createChat(user: User)

    fun createGroupChat(listOfUsers: List<User>, titleOfGroup: String)

}