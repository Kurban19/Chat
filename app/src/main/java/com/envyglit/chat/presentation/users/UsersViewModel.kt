package com.envyglit.chat.presentation.users

import androidx.lifecycle.*
import com.envyglit.chat.util.extensions.mutableLiveData
import com.envyglit.chat.domain.entities.data.UserItem
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.chat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
        private val repository: Repository
        ) : ViewModel() {

    private val query = mutableLiveData("")
    private val userItems by lazy { MutableLiveData<Resource<List<UserItem>>>() }
    private val selectedItems = Transformations.map(userItems){users -> users.data?.filter {it.isSelected}}

    private val disposable: CompositeDisposable = CompositeDisposable()


    init {
        fetchUsers()
    }

    private fun fetchUsers(){
        userItems.postValue(Resource.loading(null))
        disposable.add(
            repository.getUsers()
                .subscribeOn(Schedulers.io())
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
        val result = MediatorLiveData<Resource<List<UserItem>>>()

        val filterF = {
            val queryStr = query.value!!
            val users = if (userItems.value == null) Resource.loading(null) else userItems.value

            result.value = if(queryStr.isEmpty()) users as Resource<List<UserItem>>?
            else Resource.success(users?.data?.filter { it.fullName.contains(queryStr,true) })
        }

        result.addSource(userItems){filterF.invoke()}
        result.addSource(query){filterF.invoke()}

        return result
    }

    fun getSelectedData(): LiveData<List<UserItem>?> = selectedItems

    fun handleSelectedItem(userId: String){
        userItems.value?.data = userItems.value?.data!!.map {
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
        repository.createChat(repository.findUserById(selectedItems.value!!.first().id)!!)
    }

    fun handleCreatedGroupChat(titleOfGroup: String){
        repository.createGroupChat(repository.findUsers(selectedItems.value!!.map { it.id }.toMutableList()), titleOfGroup)
    }

    fun getSizeOfSelectedItems(): Int {
        return selectedItems.value!!.size
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}