package com.envyglit.chat.data.remote

import com.envyglit.core.data.remote.FireBaseService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.envyglit.core.data.extensions.toChat
import com.envyglit.core.ui.extensions.toUser
import com.envyglit.core.domain.entities.chat.Chat
import com.envyglit.core.domain.entities.message.BaseMessage
import com.envyglit.core.domain.entities.message.ImageMessage
import com.envyglit.core.domain.entities.message.TextMessage
import com.envyglit.core.domain.entities.user.User
import io.reactivex.Observable

class FireBaseServiceImpl : FireBaseService {

    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
            "users/${
                FirebaseAuth.getInstance().currentUser?.uid
                    ?: throw KotlinNullPointerException("UID is null")
            }"
        )

    private val usersCollectionRef = fireStoreInstance.collection(USERS)
    private val chatsCollectionRef = fireStoreInstance.collection(CHATS)
    private val messagesCollectionRef = fireStoreInstance.collection(MESSAGES)

    override fun getUsers(): Observable<List<User>> {
        return Observable.create { emitter ->
            val listOfUsers = mutableListOf<User>()
            usersCollectionRef.get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    if (user.id != FirebaseAuth.getInstance().currentUser?.uid) {
                        listOfUsers.add(user)
                    }
                }
            }.addOnSuccessListener { emitter.onNext(listOfUsers) }
        }
    }


    override fun getEngagedChats(): Observable<List<Chat>> {
        return Observable.create { emitter ->
            val listOfChats = mutableListOf<Chat>()
            currentUserDocRef.collection(ENGAGED_CHATS)
                .get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        chatsCollectionRef.document(document[CHAT_ID] as String)
                            .get().addOnSuccessListener {
                                val chat = it.toChat()
                                listOfChats.add(chat)
                            }.addOnSuccessListener { emitter.onNext(listOfChats) }
                    }
                }

        }
    }

    override fun setChatMessagesListener(
        chatId: String,
        onListen: (List<BaseMessage>) -> Unit
    ): ListenerRegistration {
        return messagesCollectionRef.document(chatId).collection(MESSAGES)
            .orderBy(DATE)
            .addSnapshotListener { querySnapshot, firebaseFireStoreException ->
                if (firebaseFireStoreException != null) {
                    return@addSnapshotListener
                }

                val items = mutableListOf<BaseMessage>()
                querySnapshot?.documents?.forEach { documentSnapshot ->

                    val message = if (documentSnapshot[TYPE] == TEXT)
                        documentSnapshot.toObject(TextMessage::class.java)
                    else
                        documentSnapshot.toObject(ImageMessage::class.java)

                    message?.let {
                        items.add(it)
                    }

                    return@forEach
                }
                onListen(items)
            }
    }

    override fun getOrCreateChat(otherUser: User) {
        currentUserDocRef.collection(ENGAGED_CHATS)
            .document(otherUser.id).get().addOnSuccessListener {
                if (it.exists()) {
                    return@addOnSuccessListener
                }

                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser ?: throw KotlinNullPointerException("Current user is null")

                val newChat = chatsCollectionRef.document()
                newChat.set(
                    Chat(
                        newChat.id,
                        newChat.id,
                        mutableListOf(currentUser.toUser(), otherUser),
                        null,
                        false
                    )
                )

                currentUserDocRef
                    .collection(ENGAGED_CHATS)
                    .document(otherUser.id)
                    .set(mapOf(CHAT_ID to newChat.id))

                fireStoreInstance.collection(USERS).document(otherUser.id)
                    .collection(ENGAGED_CHATS)
                    .document(currentUser.uid)
                    .set(mapOf(CHAT_ID to newChat.id))

            }
    }

    override fun createGroupChat(listOfUsers: List<User>, titleOfChat: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser?.toUser()
        currentUser ?: throw KotlinNullPointerException("Current user is null")
        val newChat = chatsCollectionRef.document()
        val mutableListOfUsers = listOfUsers.toMutableList()
        mutableListOfUsers.add(currentUser)
        newChat.set(Chat(newChat.id, titleOfChat, listOfUsers, null, false))

        mutableListOfUsers.forEach {
            fireStoreInstance.collection(USERS).document(it.id)
                .collection(ENGAGED_CHATS)
                .document(newChat.id)
                .set(mapOf(CHAT_ID to newChat.id))
        }

    }

    override fun updateChat(chat: Chat) {
        chatsCollectionRef
            .document(chat.id)
            .update(chat.toMap())
    }

    override fun sendMessage(message: BaseMessage, chatId: String) {
        messagesCollectionRef.document(chatId)
            .collection(MESSAGES)
            .apply {
                when (message) {
                    is TextMessage -> add(message)
                    is ImageMessage -> add(message)
                }
            }
    }

    companion object {
        private const val USERS = "users"
        private const val CHATS = "chats"
        private const val ENGAGED_CHATS = "engagedChats"
        private const val CHAT_ID = "chatId"
        private const val MESSAGES = "messages"

        private const val DATE = "date"
        private const val TYPE = "type"
        private const val TEXT = "text"
    }

}