package com.envyglit.users.domain

import com.envyglit.core.domain.entities.user.User
import io.reactivex.Observable
import javax.inject.Inject

class UsersInteractor @Inject constructor(
    private val repository: UsersRepository
) {

    fun fetchUsers(): Observable<List<User>> {
        return repository.getUsers()
    }

    fun createChat(user: User) {
        repository.createChat(user)
    }

    fun createGroupChat(users: List<User>, titleOfGroup: String) {
        repository.createGroupChat(users, titleOfGroup)
    }

    fun findUserById(userId: String): User {
        return repository.findUserById(userId)
    }

    fun findUsers(usersIds: List<String>): List<User> {
        return repository.findUsers(usersIds)
    }

}
