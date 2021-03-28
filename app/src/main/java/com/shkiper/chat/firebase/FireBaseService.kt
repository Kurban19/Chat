package com.shkiper.chat.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.shkiper.chat.extensions.toUser
import com.shkiper.chat.interfaces.FireBaseChats
import com.shkiper.chat.models.TextMessage
import com.shkiper.chat.models.data.Chat
import com.shkiper.chat.models.data.User
import java.util.*
import javax.inject.Inject

class FireBaseService @Inject constructor(): FireBaseChats {

    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )

    private val messagesCollectionRef = fireStoreInstance.collection("messages")
    private val usersCollectionRef = fireStoreInstance.collection("users")


    fun setUsersListener(onListen: (List<User>) -> Unit): ListenerRegistration {
        return usersCollectionRef
                .addSnapshotListener{ querySnapshot, firebaseFireStoreException ->
                    if (firebaseFireStoreException != null) {
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<User>()
                    querySnapshot?.documents?.forEach {
                        val user = it.toObject(User::class.java)
                        if (user != null && user.id != FirebaseAuth.getInstance().currentUser!!.uid) {
                            items.add(user)
                        }
                        return@forEach
                    }
                    onListen(items)
                }
    }

    fun updateCurrentUser(date: Date = Date(), online: Boolean) {
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["lastVisit"] = date
        userFieldMap["online"] = online
        currentUserDocRef.update(userFieldMap)
    }



    override fun getOrCreateChat(otherUser: User) {
        currentUserDocRef.collection("engagedChats")
                .document(otherUser.id).get().addOnSuccessListener {
                    if (it.exists()) {
                        return@addOnSuccessListener
                    }
                    val currentUser = FirebaseAuth.getInstance().currentUser!!


//                    val newChat = chatsCollectionRef.document()

                    val newChat = currentUserDocRef.collection("engagedChats").document()
                    val chat = Chat(newChat.id, otherUser.firstName, mutableListOf(currentUser.toUser(), otherUser), null)
                    newChat.set(chat)

//                    currentUserDocRef.collection("engagedChats")
//                            .document(newChat.id)
//                            .set(ChatId(newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document()
                            .set(chat)
                }
    }


    override fun createGroupChat(listOfUsers: MutableList<User>, titleOfChat: String){
//        val currentUser = FirebaseAuth.getInstance().currentUser!!.toUser()
//        val newChat = chatsCollectionRef.document()
//        listOfUsers.add(currentUser)
//        newChat.set(Chat(newChat.id, titleOfChat, listOfUsers, null))
//
//        currentUserDocRef.collection("engagedChats")
//                .document(newChat.id)
//                .set(ChatId(newChat.id))
//
//        listOfUsers.forEach{
//            for (user in listOfUsers){
//                if(user.id == it.id) return
//                fireStoreInstance.collection("users").document(it.id)
//                        .collection("engagedChats")
//                        .document(newChat.id)
//                        .set(ChatId(newChat.id))
//            }
//        }
    }


    override fun setEngagedChatsListener(
            onListen: (List<Chat>) -> Unit
    ): ListenerRegistration {
        return currentUserDocRef.collection("engagedChats")
                .addSnapshotListener { querySnapshot, firebaseFireStoreException ->
                    if (firebaseFireStoreException != null) {
                        return@addSnapshotListener
                    }
                    val items = mutableListOf<Chat>()
                    querySnapshot?.documents?.forEach {
                        val chat = it.toObject(Chat::class.java)
                                ?: throw KotlinNullPointerException()
                        if (chat.title == FirebaseAuth.getInstance().currentUser!!.displayName) {
                            chat.title = chat.members.last().firstName
                        }
                        items.add(chat)
                    }
                    onListen(items)
                }
    }

    override fun addChatMessagesListener(
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



    override fun updateChat(chat: Chat) {
        currentUserDocRef.collection("engagedChats")
                .document(chat.id)
                .update(chat.toMap())
        }

    override fun sendMessage(message: TextMessage, chatId: String) {
        messagesCollectionRef.document(chatId)
                .collection("messages")
                .add(message)
    }

}