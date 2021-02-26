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
import ru.skillbranch.chat.models.data.ChatId
import ru.skillbranch.chat.models.data.User
import ru.skillbranch.chat.repositories.ChatRepository
import ru.skillbranch.chat.repositories.UsersRepository
import java.util.*

object FireBaseChats {
    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private const val TAG = "FireBase"

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )

    private val chatsCollectionRef = fireStoreInstance.collection("chats")


    fun createGroupChat(listOfUsers: MutableList<User>, titleOfChat: String){
        val currentUser = FirebaseAuth.getInstance().currentUser!!.toUser()
        val newChat = chatsCollectionRef.document()
        listOfUsers.add(currentUser)
        newChat.set(Chat(newChat.id, titleOfChat, listOfUsers, null))

        currentUserDocRef.collection("engagedChats")
                .document(newChat.id)
                .set(ChatId(newChat.id))

        listOfUsers.forEach{
            for (user in listOfUsers){
                if(user.id == it.id) return
                fireStoreInstance.collection("users").document(it.id)
                        .collection("engagedChats")
                        .document(newChat.id)
                        .set(ChatId(newChat.id))
            }
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
                            .document(newChat.id)
                            .set(ChatId(newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document(newChat.id)
                            .set(ChatId(newChat.id))
                }
        getEngagedChats()
    }

    fun getEngagedChats(){
        currentUserDocRef.collection("engagedChats")
                .get().addOnSuccessListener { result ->
                    for(document in result){
                        chatsCollectionRef.document(document.id).
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