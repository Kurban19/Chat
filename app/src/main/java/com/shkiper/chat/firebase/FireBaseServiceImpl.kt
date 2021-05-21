package com.shkiper.chat.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.extensions.toChat
import com.shkiper.chat.extensions.toUser
import com.shkiper.chat.interfaces.FireBaseService
import com.shkiper.chat.model.BaseMessage
import com.shkiper.chat.model.ImageMessage
import com.shkiper.chat.model.TextMessage
import com.shkiper.chat.model.data.Chat
import com.shkiper.chat.model.data.User
import io.reactivex.Observable

class FireBaseServiceImpl: FireBaseService{

    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )

    private val usersCollectionRef = fireStoreInstance.collection("users")
    private val chatsCollectionRef = fireStoreInstance.collection("chats")
    private val messagesCollectionRef = fireStoreInstance.collection("messages")



    override fun getUsers(): Observable<List<User>>{
        return Observable.create { emitter ->
            val listOfUsers = mutableListOf<User>()
            usersCollectionRef.get().addOnSuccessListener { documents ->
                    for (document in documents){
                        val user = document.toObject(User::class.java)
                        if (user.id != FirebaseAuth.getInstance().currentUser!!.uid) {
                            listOfUsers.add(user)
                        }
                    }
                }.addOnSuccessListener { emitter.onNext(listOfUsers) }
        }
    }


    override fun getEngagedChats(): Observable<List<Chat>> {
        return Observable.create{ emitter ->
            val listOfChats = mutableListOf<Chat>()
            currentUserDocRef.collection("engagedChats")
                .get().addOnSuccessListener { documents ->
                    for (document in documents){
                        chatsCollectionRef.document(document["chatId"] as String)
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
        return messagesCollectionRef.document(chatId).collection("messages")
                .orderBy("date")
                .addSnapshotListener {  querySnapshot, firebaseFireStoreException ->
                    if (firebaseFireStoreException != null) {
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<BaseMessage>()
                    querySnapshot?.documents?.forEach { documentSnapshot ->

                        val message = if (documentSnapshot["type"] == "text")
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
        currentUserDocRef.collection("engagedChats")
                .document(otherUser.id).get().addOnSuccessListener {
                    if (it.exists()) {
                        return@addOnSuccessListener
                    }

                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRef.document()
                    newChat.set(Chat(newChat.id, newChat.id, mutableListOf(currentUser.toUser(), otherUser), null, false))

                    currentUserDocRef
                            .collection("engagedChats")
                            .document(otherUser.id)
                            .set(mapOf("chatId" to newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document(currentUser.uid)
                            .set(mapOf("chatId" to newChat.id))

                }
    }


    override fun createGroupChat(listOfUsers: MutableList<User>, titleOfChat: String){
        val currentUser = FirebaseAuth.getInstance().currentUser!!.toUser()
        val newChat = chatsCollectionRef.document()
        listOfUsers.add(currentUser)
        newChat.set(Chat(newChat.id, titleOfChat, listOfUsers, null, false))

        listOfUsers.forEach {
            fireStoreInstance.collection("users").document(it.id)
                .collection("engagedChats")
                .document(newChat.id)
                .set(mapOf("chatId" to newChat.id))
        }

    }


    override fun updateChat(chat: Chat) {
        chatsCollectionRef
                .document(chat.id)
                .update(chat.toMap())
        }

    override fun sendMessage(message: BaseMessage, chatId: String) {
        messagesCollectionRef.document(chatId)
                .collection("messages")
                .apply {
                    when(message){
                        is TextMessage -> add(message)
                        is ImageMessage -> add(message)
                    }
                }
    }

}