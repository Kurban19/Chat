package com.shkiper.chat.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.model.data.UserItem
import com.shkiper.chat.repositories.MainRepository
import com.shkiper.chat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
        private val mainRepository: MainRepository
        ) : ViewModel() {
    private val disposable: CompositeDisposable = CompositeDisposable()
    private val query = mutableLiveData("")
//    private val userItems = mutableLiveData(mainRepository.users.value!!.map{it.toUserItem()})
    private val userItems by lazy { MutableLiveData<Resource<List<UserItem>>>() }
    private val selectedItems = Transformations.map(userItems){users -> users.data?.filter {it.isSelected}}


    init {
        fetchUsers()
    }

    private fun fetchUsers(){
        userItems.postValue(Resource.loading(null))
        disposable.add(
            mainRepository.getUsers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { listOfUsers -> listOfUsers.map { it.toUserItem() } }
                .subscribe ({
                    userItems.postValue(Resource.success(it))
                            },{
                    userItems.postValue(Resource.error(it.printStackTrace().toString(),null))
                })
        )
    }



    fun getUsers(): MutableLiveData<Resource<List<UserItem>>> {
//        val result = MediatorLiveData<Resource<List<UserItem>>>()
//
//        val filterF = {
//            val queryStr = query.value!!
//            val users = userItems.value!!
//
//            result.value = if(queryStr.isEmpty()) users
//            else Resource.success(users.data?.filter { it.fullName.contains(queryStr,true) })
//        }
//
//        result.addSource(userItems){filterF.invoke()}
//        result.addSource(query){filterF.invoke()}
//
//        return result
        return userItems
    }

    fun getSelectedData(): LiveData<List<UserItem>?> = selectedItems

    fun handleSelectedItem(userId: String){
        userItems.value!!.data = userItems.value!!.data!!.map {
            if(it.id == userId) it.copy(isSelected = !it.isSelected)
            else it
        }
    }

    fun handleRemoveChip(userId: String) {
        userItems.value!!.data = userItems.value!!.data!!.map {
            if (it.id == userId) it.copy(isSelected = false)
            else it
        }
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }


    fun handleCreatedChat() {
        mainRepository.createChat(mainRepository.findUser(selectedItems.value!!.first().id)!!)
        mainRepository.updateData()
    }

    fun handleCreatedGroupChat(titleOfGroup: String){
        mainRepository.createGroupChat(mainRepository.findUsers(selectedItems.value!!.map { it.id }.toMutableList()), titleOfGroup)
        mainRepository.updateData()
    }

    fun getSizeOfSelectedItems(): Int {
        return selectedItems.value!!.size
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}