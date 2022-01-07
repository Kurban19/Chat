package com.envyglit.chat.presentation.activities.users

import androidx.lifecycle.*
import com.envyglit.core.ui.entities.user.UserItem
import com.envyglit.core.ui.extensions.mutableLiveData
import com.envyglit.core.ui.utils.Resource
import com.envyglit.users.domain.UsersInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class UsersUiState(
    val userItems: List<UserItem> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class UsersViewModel @Inject constructor(
        private val interactor: UsersInteractor
        ) : ViewModel() {

    private val _uiState = MutableStateFlow(UsersUiState(loading = true))
    val uiState: StateFlow<UsersUiState> = _uiState.asStateFlow()

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
            interactor.fetchUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { listOfUsers -> listOfUsers.map { it.toUserItem() } }
                .subscribe ({ listOfUser ->
                    _uiState.update { it.copy(userItems = listOfUser, loading = false) }
//                    userItems.postValue(Resource.success(it))
                            },{
                    _uiState.update { it.copy(errorMessage = it.errorMessage) }
//                    userItems.postValue(Resource.error(it.printStackTrace().toString(),null))
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
        interactor.createChat(interactor.findUserById(selectedItems.value!!.first().id))
    }

    fun handleCreatedGroupChat(titleOfGroup: String){
        interactor.createGroupChat(interactor.findUsers(selectedItems.value!!.map { it.id }.toMutableList()), titleOfGroup)
    }

    fun getSizeOfSelectedItems(): Int {
        return selectedItems.value?.size ?: 0
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

}
