package com.shkiper.chat.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.extensions.toUser
import com.shkiper.chat.interfaces.FireBaseChats
import com.shkiper.chat.interfaces.FireBaseUsers
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.models.data.User
import java.util.*
import javax.inject.Inject

class FireBaseService @Inject constructor(): FireBaseChats, FireBaseUsers {

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
                }
        onListen(listOfUsers)
    }


    override fun getEngagedChats(onListen: (List<Chat>) -> Unit) {
        currentUserDocRef.collection("engagedChats")
                .get().addOnSuccessListener { documents ->
                    val listOfChats = mutableListOf<Chat>()
                    for (document in documents){
                        chatsCollectionRef.document(document["chatId"] as String)
                                .get().addOnSuccessListener {
                                    val chat = it.toObject(Chat::class.java) ?: throw KotlinNullPointerException()
                                    listOfChats.add(chat)
                                }.addOnSuccessListener { onListen(listOfChats) }
                    }
                }
    }


    override fun setChatMessagesListener(
            chatId: String,
            onListen: (List<TextMessage>) -> Unit
    ): ListenerRegistration {
        return messagesCollectionRef.document(chatId).collection("messages")
                .orderBy("date")
                .addSnapshotListener {  querySnapshot, firebaseFireStoreException ->
                    if (firebaseFireStoreException != null) {
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<TextMessage>()
                    querySnapshot?.documents?.forEach {
                        val message = it.toObject(TextMessage::class.java)
                        if (message != null) {
                            items.add(message)
                        }
                        return@forEach
                    }
                    onListen(items)
                }
    }



    override fun getOrCreateChat(otherUser: User) {
        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUser.id).get().addOnSuccessListener {
                    if (it.exists()) {
                        return@addOnSuccessListener
                    }

                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRef.document()
                    newChat.set(Chat(newChat.id, newChat.id, mutableListOf(currentUser.toUser(), otherUser), null))

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
        newChat.set(Chat(newChat.id, titleOfChat, listOfUsers, null))

        currentUserDocRef.collection("engagedChats")
                .document(newChat.id)
                .set(mapOf("chatId" to newChat.id))

        listOfUsers.forEach{
            for (user in listOfUsers){
                if(user.id == it.id) return
                fireStoreInstance.collection("users").document(it.id)
                        .collection("engageChats")
                        .document(newChat.id)
                        .set(mapOf("chatId" to newChat.id))
            }
        }
    }


    override fun updateChat(chat: Chat) {
        chatsCollectionRef
                .document(chat.id)
                .update(chat.toMap())
        }

    override fun sendMessage(message: TextMessage, chatId: String) {
        messagesCollectionRef.document(chatId)
                .collection("messages")
                .add(message)
    }

}