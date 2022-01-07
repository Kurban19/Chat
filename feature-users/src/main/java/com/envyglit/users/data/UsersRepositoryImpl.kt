package com.envyglit.users.data

import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.core.domain.entities.user.User
import com.envyglit.users.domain.UsersRepository
import io.reactivex.Observable

class UsersRepositoryImpl(private val fireBaseService: FireBaseService): UsersRepository {

    override fun getUsers(): Observable<List<User>> {
        TODO("Not yet implemented")
    }

    override fun findUsers(userIds: List<String>): List<User> {
        TODO("Not yet implemented")
    }

    override fun findUserById(userId: String): User {
        TODO("Not yet implemented")
    }

    override fun createChat(user: User) {
        TODO("Not yet implemented")
    }

    override fun createGroupChat(listOfUsers: List<User>, titleOfGroup: String) {
        TODO("Not yet implemented")
    }
}