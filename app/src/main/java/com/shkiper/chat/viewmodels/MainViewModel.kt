package com.shkiper.chat.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.shkiper.chat.extensions.mutableLiveData
import com.shkiper.chat.extensions.shortFormat
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.ChatItem
import com.shkiper.chat.model.data.ChatType
import com.shkiper.chat.model.data.UserItem
import com.shkiper.chat.repositories.MainRepository
import com.shkiper.chat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
    ): ViewModel() {

    private val query = mutableLiveData("")
//    private val chats = MutableLiveData<Resource<List<ChatItem>>>(Resource.loading(null))
    private val chats by lazy { MutableLiveData<Resource<List<ChatItem>>>() }

    private val disposable: CompositeDisposable = CompositeDisposable()



    init {
        fetchChats()
    }


    private fun fetchChats(){
        //        chats = Transformations.map(mainRepository.chats) { chats ->
//            val archived = chats.filter { it.archived }
//            if (archived.isEmpty()) {
//                return@map Resource.success(chats.map { it.toChatItem() })
//            } else {
//                val listWithArchive = mutableListOf<ChatItem>()
//                listWithArchive.add(0, makeArchiveItem(archived))
//                listWithArchive.addAll((chats.filter { !it.archived }.map { it.toChatItem() }))
//                return@map Resource.success(listWithArchive)
//            }
//        } as MutableLiveData<Resource<List<ChatItem>>>

//        val chatData = mainRepository.chats
//        val archived = chatData.value?.filter { it.archived } ?: emptyList()
//        if(archived.isEmpty()){
//            chats.postValue(Resource.success(chatData.value!!.map { it.toChatItem() }))
//        }
//        else{
//            Log.d("MAView", chatData.toString())
//            val listWithArchive = mutableListOf<ChatItem>()
//            listWithArchive.add(0, makeArchiveItem(archived))
//            listWithArchive.addAll((chatData.value!!.filter { !it.archived }.map { it.toChatItem() }))
//            chats.postValue(Resource.success(listWithArchive))
//        }



        chats.postValue(Resource.loading(null))
        disposable.add(
            mainRepository.getEngagedChats()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map { listOfChats -> listOfChats.map { it.toChatItem() } }
                .subscribe ({
                    chats.postValue(Resource.success(it))
                },{
                    chats.postValue(Resource.error(it.printStackTrace().toString(),null))
                })
        )
    }

    fun getChatData() : MutableLiveData<Resource<List<ChatItem>>> {
//        val result = MediatorLiveData<Resource<List<ChatItem>>>()
//
//        val filterF = {
//            val queryStr = query.value!!
//            val chats = chats.value
//
//            result.value = if(queryStr.isEmpty()) chats
//            else Resource.success(chats!!.data!!.filter { it.title.contains(queryStr,true) })
//        }
//
//        result.addSource(chats){filterF.invoke()}
//        result.addSource(query){filterF.invoke()}
//
//        return result



        return chats

    }

    fun addToArchive(chatId: String) {
        val chat = mainRepository.findChat(chatId)
        mainRepository.updateChat(chat.copy(archived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = mainRepository.findChat(chatId)
        mainRepository.updateChat(chat.copy(archived = false))
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }


    private fun makeArchiveItem(archived: List<Chat>): ChatItem {
        val count = archived.fold(0) { acc, chat -> acc + chat.unreadMessageCount() }

        val lastChat: Chat =
                if (archived.none { it.unreadMessageCount() != 0 }) archived.last() else
                    archived.filter { it.unreadMessageCount() != 0 }
                        .maxByOrNull { it.lastMessageDate()!! }!!

        return ChatItem(
                "-1",
                null,
                "",
                "Архив чатов",
                lastChat.lastMessageShort().first,
                count,
                lastChat.lastMessageDate()?.shortFormat(),
                false,
                ChatType.ARCHIVE,
                lastChat.lastMessageShort().second
        )
    }


}