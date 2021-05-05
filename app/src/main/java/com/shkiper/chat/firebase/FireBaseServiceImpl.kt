package com.shkiper.chat.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.interfaces.IFireBaseService
import com.shkiper.chat.models.BaseMessage
import com.shkiper.chat.models.ImageMessage
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.models.data.User
import java.util.*
import javax.inject.Inject

class FireBaseServiceImpl @Inject constructor(): IFireBaseService{

    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )

    private val usersCollectionRef = fireStoreInstance.collection("users")
    private val chatsCollectionRef = fireStoreInstance.collection("chats")
    private val messagesCollectionRef = fireStoreInstance.collection("messages")

    override fun getUsers(onListen: (List<User>) -> Unit) {
        val listOfUsers = mutableListOf<User>()
        usersCollectionRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val user = document.toObject(User::class.java)
                        if (user.id != FirebaseAuth.getInstance().currentUser!!.uid) {
                            listOfUsers.add(user)
                        }
                    }
                }.addOnSuccessListener { onListen(listOfUsers)}

    }


    override fun getEngagedChats(onListen: (List<Chat>) -> Unit) {
        currentUserDocRef.collection("engagedChats")
                .get().addOnSuccessListener { documents ->
                    val listOfChats = mutableListOf<Chat>()
                    for (document in documents){
                        chatsCollectionRef.document(document["chatId"] as String)
                                .get().addOnSuccessListener {
                                    val chat = it.toObject(Chat::class.java)
                                    if(chat != null){
                                        listOfChats.add(chat)
                                    }
                                }.addOnSuccessListener { onListen(listOfChats) }
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
                    querySnapshot?.documents?.forEach {
//                        val message = it.toObject(TextMessage::class.java)

                        val message = if (it["type"] == "text")
                            it.toObject(TextMessage::class.java)
                        else
                            it.toObject(ImageMessage::class.java)

                        message?.let {
                            items.add(it)
                        }

                        return@forEach
                    }
                    onListen(items)
                }
    }



    override fun getOrCreateChat(otherUserId: String) {
        currentUserDocRef.collection("engagedChats")
                .document(otherUserId).get().addOnSuccessListener {
                    if (it.exists()) {
                        return@addOnSuccessListener
                    }

                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRef.document()
                    newChat.set(Chat(newChat.id, newChat.id, mutableListOf(currentUser.uid, otherUserId), null, false))

                    currentUserDocRef
                            .collection("engagedChats")
                            .document(otherUserId)
                            .set(mapOf("chatId" to newChat.id))

                    fireStoreInstance.collection("users").document(otherUserId)
                            .collection("engagedChats")
                            .document(currentUser.uid)
                            .set(mapOf("chatId" to newChat.id))

                }
    }


    override fun createGroupChat(listOfUsersIds: MutableList<String>, titleOfChat: String){
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val newChat = chatsCollectionRef.document()
        listOfUsersIds.add(currentUser.uid)
        newChat.set(Chat(newChat.id, titleOfChat, listOfUsersIds, null, false))

        listOfUsersIds.forEach {
            fireStoreInstance.collection("users").document(it)
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