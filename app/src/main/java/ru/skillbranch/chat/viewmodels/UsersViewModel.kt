package ru.skillbranch.chat.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.chat.extensions.mutableLiveData
import ru.skillbranch.chat.firebase.FireBaseChats
import ru.skillbranch.chat.models.data.UserItem
import ru.skillbranch.chat.repositories.UsersRepository

class UsersViewModel : ViewModel() {
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
            FireBaseChats.createGroupChat(usersRepository.findUsersById(selectedItems.value!!.map { it.id }).toMutableList(), "Test")
        }
        else{
            FireBaseChats.getOrCreateChat(usersRepository.findUser(selectedItems.value!!.first().id)!!)
        }
    }


}