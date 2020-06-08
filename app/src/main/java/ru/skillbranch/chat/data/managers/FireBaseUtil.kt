package ru.skillbranch.chat.data.managers

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
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
import ru.skillbranch.chat.repositories.GroupRepository
import java.util.*


object FireBaseUtil {
    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val groupRepository = GroupRepository


    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )
    private val chatChannelsCollectionRef = fireStoreInstance.collection("chatChannels")
    private val chatsCollectionRef = fireStoreInstance.collection("chats")


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

    fun getChats(): MutableList<Chat> {
        val items = mutableListOf<Chat>()

        fireStoreInstance.collection("chats")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val chat = document.toObject(Chat::class.java)
                        if (FirebaseAuth.getInstance().currentUser!!.uid == chat.members[0].id) {
                            //items.add(document.toObject(Chat::class.java))
                            CacheManager.insertChat(chat)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        return items
    }

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                        FirebaseAuth.getInstance().currentUser!!.uid, FirebaseAuth.getInstance().currentUser?.displayName
                        ?: "",
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


    fun getOrCreateChat(otherUser: User) {

        currentUserDocRef.collection("engagedChats")
                .document(otherUser.id).get().addOnSuccessListener {
                    if (it.exists()) {
                        //onComplete(it["channelId"] as String)
                        return@addOnSuccessListener
                    }
                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRef.document()
                    newChat.set(Chat(newChat.id, otherUser.firstName!!, mutableListOf(currentUser.toUser(), otherUser), mutableListOf(TextMessage("fldskjf3q324", FirebaseAuth.getInstance().currentUser!!.toUser(), isRead = false, isIncoming = true, date = Date(), type = "text", text = "test for send messages"))))

                    currentUserDocRef.collection("engagedChats")
                            .document(otherUser.id)
                            .set(mapOf("channelId" to newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document(currentUser.uid)
                            .set(mapOf("channelId" to newChat.id))
                }
    }

    private fun getChat(id: String) {
        currentUserDocRef.collection("engagedChats")
                .document(id).get().addOnSuccessListener {
                    if (it.exists()) {
                        fireStoreInstance.collection("chats").document(it["channelId"] as String)
                                .get()
                                .addOnSuccessListener { result ->
                                    CacheManager.insertChat(result.toObject(Chat::class.java)!!)
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "Error getting documents: ", exception)
                                }
                        return@addOnSuccessListener
                    }

                }
    }

    fun getChats1() {
        val users = groupRepository.loadUsers()
        //Log.d(TAG, users.size.toString())

//        for (i in 0..users.size) {
//            Log.d(TAG, users.size as String)
//            val id = users[i].id
//            getChat(id)
//        }
    }


        fun getMessages(channelId: String): MutableList<BaseMessage> {
            val items = mutableListOf<BaseMessage>()
            chatChannelsCollectionRef.document(channelId).collection("messages")
                    .orderBy("time")
                    .get()
                    .addOnSuccessListener { it ->
                        for (document in it) {
                            if (document["type"] == "text") {
                                items.add(document.toObject(TextMessage::class.java))
                            } else
                                items.add(document.toObject(ImageMessage::class.java))
                        }
                    }
            return items
        }


        fun sendMessage(message: BaseMessage, channelId: String) {
            chatChannelsCollectionRef.document(channelId)
                    .collection("messages")
                    .add(message)
        }

        fun sendMessageChat(chat: Chat) {
            chatsCollectionRef.document(chat.id)
                    .update(chat.toMap())

        }

}