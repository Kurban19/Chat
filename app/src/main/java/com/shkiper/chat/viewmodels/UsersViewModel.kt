package com.shkiper.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.models.data.UserItem
import com.shkiper.chat.repositories.ChatsRepository
import com.shkiper.chat.repositories.UsersRepository
import javax.inject.Inject

class UsersViewModel @Inject constructor(private val chatsRepository: ChatsRepository) : ViewModel() {
    private val query = mutableLiveData("")
    private val usersRepository = UsersRepository
    private val userItems = mutableLiveData(usersRepository.loadUsers().value!!.map{it.toUserItem()})
    private val selectedItems = Transformations.map(userItems){users -> users.filter {it.isSelected}}

    fun getUsersData(): LiveData<List<UserItem>>{
        val result = MediatorLiveData<List<UserItem>>()

        val filterF = {
            val queryStr = query.value!!
            val users = userItems.value!!

            result.value = if(queryStr.isEmpty()) users
            else users.filter { it.fullName.contains(queryStr,true) }
        }

        result.addSource(userItems){filterF.invoke()}
        result.addSource(query){filterF.invoke()}

        return result
    }

    fun getSelectedData(): LiveData<List<UserItem>> = selectedItems

    fun handleSelectedItem(userId: String){
        userItems.value = userItems.value!!.map {
            if(it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }
    }

    fun handleRemoveChip(userId: String) {
        userItems.value = userItems.value!!.map {
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }


    fun handleCreatedGroup() {
        if(selectedItems.value!!.size > 1){
//            FireBaseChatsImpl.createGroupChat(usersRepository.findUsersById(selectedItems.value!!.map { it.id }).toMutableList(), "Test")
        }
        else{
//            FireBaseChatsImpl.getOrCreateChat(usersRepository.findUser(selectedItems.value!!.first().id)!!)
            chatsRepository.createChat(usersRepository.findUser(selectedItems.value!!.first().id)!!)
        }
    }


}