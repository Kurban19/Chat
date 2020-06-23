package ru.skillbranch.chat.data.managers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.skillbranch.chat.extensions.toUser
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


    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                with(FirebaseAuth.getInstance().currentUser){
                    val newUser = User(this!!.uid, displayName ?: "unknown", "", email ?: "", Date())

                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                    }
                }
            } else
                onComplete()
        }
    }

    fun updateCurrentUser(date: Date = Date(), online: Boolean) {
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["lastVisit"] = date
        userFieldMap["online"] = online
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
                        return@addOnSuccessListener
                    }
                    val currentUser = FirebaseAuth.getInstance().currentUser!!

                    val newChat = chatsCollectionRef.document()
                    newChat.set(Chat(newChat.id, otherUser.firstName!!, mutableListOf(currentUser.toUser(), otherUser), mutableListOf()))

                    currentUserDocRef.collection("engagedChats")
                            .document(otherUser.id)
                            .set(mapOf("channelId" to newChat.id))

                    fireStoreInstance.collection("users").document(otherUser.id)
                            .collection("engagedChats")
                            .document(currentUser.uid)
                            .set(mapOf("channelId" to newChat.id))
                    //getChat(newChat.id)
                    getEngagedChats()

                }
    }

    fun getEngagedChats(){
        currentUserDocRef.collection("engagedChats")
                .get().addOnSuccessListener { result ->
                    for(document in result){
                        fireStoreInstance.collection("chats").document(document["channelId"] as String).
                        get().addOnSuccessListener { result ->
                            val chat = result.toObject(Chat::class.java)!!
                            if(chat.title == FirebaseAuth.getInstance().currentUser!!.displayName){
                                chat.members.forEach{
                                    if(it.id != FirebaseAuth.getInstance().currentUser!!.uid){
                                        chat.title = it.firstName ?: "Unknown"
                                    }
                                }
                            }
                            if(CacheManager.haveChat(chat.id)){
                                CacheManager.update(chat)
                            }
                            else{
                                CacheManager.insertChat(chat)
                            }
                        }
                    }

                }
    }


        fun sendMessageChat(chat: Chat) {
            chatsCollectionRef.document(chat.id)
                    .update(chat.toMap())
        }

}