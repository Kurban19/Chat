package com.envyglit.main.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.chat.ChatType
import com.envyglit.core.ui.entities.chat.ChatItem
import com.envyglit.core.ui.extensions.mutableLiveData
import com.envyglit.core.ui.extensions.shortFormat
import com.envyglit.core.ui.utils.Resource
import com.envyglit.main.domain.MainInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class HomeUiState(
    val chatItems: List<ChatItem> = emptyList(),
    val loading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val interactor: MainInteractor
) : ViewModel() {

    private val query = mutableLiveData("")

    private val chats by lazy { MutableLiveData<Resource<List<ChatItem>>>() }

    private val _uiState = MutableStateFlow(HomeUiState(loading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val disposable: CompositeDisposable = CompositeDisposable()

    init {
        fetchChats()
    }

    private fun fetchChats() {
        _uiState.update { it.copy(loading = true) }
        disposable.add(
            interactor.getChats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data ->
                    val archived = data.filter { it.archived }
                    if (archived.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                chatItems = data.map { chat -> chat.toChatItem() },
                                loading = false
                            )
                        }
                    } else {
                        val listWithArchive = mutableListOf<ChatItem>()
                        listWithArchive.add(0, makeArchiveItem(archived))
                        listWithArchive.addAll((data.filter { !it.archived }
                            .map { chat -> chat.toChatItem() }))
                        _uiState.update { it.copy(chatItems = listWithArchive, loading = false) }
                    }
                }, { throwable ->
                    _uiState.update { it.copy(errorMessage = throwable.message, loading = false) }
                })
        )
    }

    fun getChatData(): MutableLiveData<Resource<List<ChatItem>>> {
        val result = MediatorLiveData<Resource<List<ChatItem>>>()

        val filterF = {
            val queryStr = query.value.orEmpty()
            val chats = if (chats.value == null) Resource.loading(null) else chats.value

            result.value = if (queryStr.isEmpty()) chats as Resource<List<ChatItem>>?
            else Resource.success(chats?.data?.filter { it.title.contains(queryStr, true) })
        }

        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }

        return result
    }

    fun addToArchive(chatId: String) {
        val chat = interactor.findChatById(chatId)
        interactor.updateChat(chat.copy(archived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = interactor.findChatById(chatId)
        interactor.updateChat(chat.copy(archived = false))
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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
