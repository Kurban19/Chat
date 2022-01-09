package com.envyglit.users.data

import androidx.lifecycle.MutableLiveData
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.core.domain.entities.user.User
import com.envyglit.users.domain.UsersRepository
import io.reactivex.Observable

class UsersRepositoryImpl(private val fireBaseService: FireBaseService): UsersRepository {

    val users = MutableLiveData<List<User>>(listOf())

    override fun getUsers(): Observable<List<User>> {
        return fireBaseService.getUsers()
            .doOnNext {
                users.value = it
            }
    }

    override fun findUsers(userIds: List<String>): List<User> {
        return users.value!!.filter { userIds.contains(it.id) }.toMutableList()
    }

    override fun findUserById(userId: String): User {
        val user = users.value?.find { it.id == userId }
        user ?: throw KotlinNullPointerException("User not found")
        return user
    }

    override fun createChat(user: User) {
        fireBaseService.getOrCreateChat(user)
    }

    override fun createGroupChat(listOfUsers: List<User>, titleOfGroup: String) {
        fireBaseService.createGroupChat(listOfUsers, titleOfGroup)
    }
}
