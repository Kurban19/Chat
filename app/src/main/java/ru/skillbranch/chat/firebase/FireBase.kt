package ru.skillbranch.chat.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.BaseMessage
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.repositories.UsersRepository
import java.util.*

object FireBase {
    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private const val TAG = "FireBase"

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )
    private val chatsCollectionRef = fireStoreInstance.collection("chats")


    fun getAllDataFromServer(onComplete: (() -> Unit)){
        getEngagedChats()
        getUsers()
        onComplete()
    }

    private fun getUsers(){
        fireStoreInstance.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    Log.d(TAG, result.documents.size.toString())
                    for (document in result) {
                        if (document.id != FirebaseAuth.getInstance().currentUser!!.uid)
                            UsersRepository.addUser(document.toObject(User::class.java))
                    }
                }
    }


    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                with(FirebaseAuth.getInstance().currentUser){
                    val newUser = User(this!!.uid, displayName ?: "unknown", "", email = email ?: "")

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    getAllDataFromServer(onComplete)
                    }
                }
            } else
                getAllDataFromServer(onComplete)
        }
    }

    fun updateCurrentUser(date: Date = Date(), online: Boolean) {
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["lastVisit"] = date
        userFieldMap["online"] = online
        currentUserDocRef.update(userFieldMap)
    }

    fun getOrCreateGroupChat(listOfUsers: MutableList<User>, titleOfChat: String){
        val currentUser = FirebaseAuth.getInstance().currentUser!!

        val newChat = chatsCollectionRef.document()
        listOfUsers.add(currentUser.toUser())
        newChat.set(Chat(newChat.id, titleOfChat, listOfUsers, null))

        listOfUsers.forEach {
            currentUserDocRef.collection("engagedChats")
                    .document(it.id)
                    .set(mapOf("channelId" to newChat.id))
        }

        listOfUsers.forEach {
            fireStoreInstance.collection("users").document(it.id)
                    .collection("engagedChats")
                    .document(currentUser.uid)
                    .set(mapOf("channelId" to newChat.id))
        }

        getEngagedChats()

    }


    fun getOrCreateChat(otherUser: User) {
        currentUserDocRef.collection("engagedChats")
                .document(otherUser.id).get().addOnSuccessListener {
                    if (it.exists()) {
                        return@addOnSuccessListener
                    }
                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRef.document()
                    newChat.set(Chat(newChat.id, otherUser.firstName, mutableListOf(currentUser.toUser(), otherUser), null))

                    currentUserDocRef.collection("engagedChats")
                            .document(otherUser.id)
                            .set(mapOf("channelId" to newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document(currentUser.uid)
                            .set(mapOf("channelId" to newChat.id))
                            getEngagedChats()
                }
    }

    private fun getEngagedChats(){
        currentUserDocRef.collection("engagedChats")
                .get().addOnSuccessListener { result ->
                    for(document in result){
                        chatsCollectionRef.document(document["channelId"] as String).
                        get().addOnSuccessListener { result ->
                            if(!result.exists()){
                                return@addOnSuccessListener
                            }
                            val chat = result.toObject(Chat::class.java)!!
                            if(chat.title == FirebaseAuth.getInstance().currentUser!!.displayName){
                                chat.members.forEach{
                                    if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                                        chat.title = it.firstName
                                    }
                                }
                            }
                            if(ChatRepository.haveChat(chat.id)){
                                ChatRepository.updateChat(chat)
                            }
                            else{
                                ChatRepository.insertChat(chat)
                            }
                        }
                    }
                }
    }

    fun addChatMessagesListener(
            chatId: String,
            onListen: (List<TextMessage>) -> Unit
    ): ListenerRegistration {
        return chatsCollectionRef.document(chatId).collection("messages")
                .orderBy("date")
                .addSnapshotListener { querySnapshot, firebaseFireStoreException ->
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


    fun getUnreadMessages(chatId: String): Int {
        val result = mutableListOf<BaseMessage>()
        chatsCollectionRef.document(chatId).collection("messages")
                .get()
                .addOnSuccessListener { querySnapshot ->
                    querySnapshot.documents.forEach {
                        val message = it.toObject(TextMessage::class.java)
                        if (message != null) {
                            result.add(message)
                        }
                        return@forEach
                    }
                }
        return result.filter { !it.isRead }.size
    }


    fun updateChat(chat: Chat) {
        chatsCollectionRef.document(chat.id)
                .update(chat.toMap())
        }

    fun sendMessage(message: TextMessage, chatId: String) {
        chatsCollectionRef.document(chatId)
                .collection("messages")
                .add(message)
    }

}