package com.envyglit.chat.presentation.archive

import androidx.lifecycle.*
import com.envyglit.chat.util.extensions.mutableLiveData
import com.envyglit.chat.domain.entities.data.ChatItem
import com.envyglit.chat.domain.interactors.ChatsInteractor
import com.envyglit.chat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class ArchiveViewModel @Inject constructor(
        private val interactor: ChatsInteractor
        ): ViewModel() {
    private val query = mutableLiveData("")
    private val chats by lazy { MutableLiveData<Resource<List<ChatItem>>>() }

    private val disposable: CompositeDisposable = CompositeDisposable()


    init {
        fetchChats()
    }


    private fun fetchChats(){
        chats.postValue(Resource.loading(null))
        disposable.add(
            interactor.getChats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ data ->
                    chats.postValue(Resource.success(data.filter{it.archived}.map { chat ->  chat.toChatItem() }))},
                    {
                    chats.postValue(Resource.error(it.printStackTrace().toString(),null))
                })
        )
    }

    fun getChatData() : MutableLiveData<Resource<List<ChatItem>>> {
        val result = MediatorLiveData<Resource<List<ChatItem>>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = if (chats.value == null) Resource.loading(null) else chats.value

            result.value = if(queryStr.isEmpty()) chats as Resource<List<ChatItem>>?
            else Resource.success(chats?.data?.filter { it.title.contains(queryStr,true) })
        }

        result.addSource(chats){filterF.invoke()}
        result.addSource(query){filterF.invoke()}

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = interactor.findChatById(chatId)
        interactor.updateChat(chat.copy(archived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = interactor.findChatById(chatId)
        interactor.updateChat(chat.copy(archived = false))
    }

}