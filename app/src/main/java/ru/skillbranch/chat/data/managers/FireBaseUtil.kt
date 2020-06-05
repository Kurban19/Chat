package ru.skillbranch.chat.data.managers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import ru.skillbranch.chat.extensions.toUser
import ru.skillbranch.chat.models.BaseMessage
import ru.skillbranch.chat.models.ChatChannel
import ru.skillbranch.chat.models.ImageMessage
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.Chat
import ru.skillbranch.chat.models.data.User
import java.util.*


object FireBaseUtil {
    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )


    private val chatChannelsCollectionRef = fireStoreInstance.collection("chatChannels")
    private val chatsCollectionRefB = fireStoreInstance.collection("chats")



    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                        FirebaseAuth.getInstance().currentUser!!.uid, FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "", FirebaseAuth.getInstance().currentUser!!.email!!, Date())

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }

    fun updateCurrentUser(name: String = "", bio: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }

    fun getOrCreateChatChannel(
            otherUserId: String,
            onComplete: (channelId: String) -> Unit){

        currentUserDocRef.collection("engagedChatChannels")
                .document(otherUserId).get().addOnSuccessListener {
                    if (it.exists()) {
                        onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }
                    val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

                    val newChannel = chatChannelsCollectionRef.document()
                    newChannel.set(ChatChannel(mutableListOf(currentUserId, otherUserId)))

                    currentUserDocRef
                            .collection("engagedChatChannels")
                            .document(otherUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    fireStoreInstance.collection("users").document(otherUserId)
                            .collection("engagedChatChannels")
                            .document(currentUserId)
                            .set(mapOf("channelId" to newChannel.id))

                    onComplete(newChannel.id)
                }
    }

    fun getOrCreateChat(otherUser: User){

        currentUserDocRef.collection("engagedChats")
                .document(otherUser.id).get().addOnSuccessListener {
                    if (it.exists()){
                        //onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }
                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRefB.document()
                    newChat.set(Chat(newChat.id, otherUser.firstName!!, mutableListOf(currentUser.toUser(), otherUser), mutableListOf(), false))

                    currentUserDocRef.collection("engagedChats")
                            .document(otherUser.id)
                            .set(mapOf("channelId" to newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document(currentUser.uid)
                            .set(mapOf("channelId" to newChat.id))
                }
    }


    fun getMessages(channelId: String): MutableList<BaseMessage>{
        val items = mutableListOf<BaseMessage>()
        chatChannelsCollectionRef.document(channelId).collection("messages")
                .orderBy("time")
                .get()
                .addOnSuccessListener { it ->
                    for(document in it) {
                        if (document["type"] == "text") {
                            items.add(document.toObject(TextMessage::class.java))
                        }
                        else
                            items.add(document.toObject(ImageMessage::class.java))
                    }
                }
        return items
    }


    fun getUsers(): MutableList<User> {
        val items = mutableListOf<User>()
        fireStoreInstance.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id != FirebaseAuth.getInstance().currentUser!!.uid)
                            items.add(document.toObject(User::class.java))
                    }
                }
        return items
    }

    fun addChatMessagesListener(
            channelId: String,
            onListen: (List<BaseMessage>) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
                .orderBy("time")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<BaseMessage>()
                    querySnapshot!!.documents.forEach {
                        if (it["type"] == "text")
                            items.add(it.toObject(TextMessage::class.java)!!)
                        else
                            items.add(it.toObject(ImageMessage::class.java)!!)
                        return@forEach
                    }
                    onListen(items)
                }
    }



    fun sendMessage(message: BaseMessage, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
                .collection("messages")
                .add(message)
    }

}