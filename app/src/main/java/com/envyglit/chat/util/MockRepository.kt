package com.envyglit.chat.util

import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.user.User
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.utils.DataGenerator
import com.google.firebase.firestore.ListenerRegistration
import io.reactivex.Observable

class MockRepository : Repository {
    override fun getChats(): Observable<List<Chat>> {
        return Observable.create{ DataGenerator.stabChats}
    }

    override fun findChatById(chatId: String): Chat? {
        return DataGenerator.stabChats.find { it?.id == chatId }
    }

    override fun createChat(user: User) {
        TODO("Not yet implemented")
    }

    override fun createGroupChat(listOfUsers: MutableList<User>, titleOfGroup: String) {
        TODO("Not yet implemented")
    }

    override fun updateChat(chat: Chat) {
        TODO("Not yet implemented")
    }

    override fun getUsers(): Observable<List<User>> {
        return Observable.create{ DataGenerator.stabUsers}
    }

    override fun findUserById(userId: String): User? {
        return DataGenerator.stabUsers.find { it.id == userId }
    }

    override fun findUsers(ids: List<String>): MutableList<User> {
        return DataGenerator.stabUsers.filter { ids.contains(it.id) }.toMutableList()
    }

    override fun sendMessage(message: BaseMessage, chatId: String) {
        TODO("Nothing")
    }

    override fun addMessagesListener(
        chatId: String,
        onListen: (List<BaseMessage>) -> Unit
    ): ListenerRegistration {
        TODO("Nothing")
    }
}