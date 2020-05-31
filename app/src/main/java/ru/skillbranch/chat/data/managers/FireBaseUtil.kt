package ru.skillbranch.chat.data.managers

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import ru.skillbranch.chat.models.TextMessage
import ru.skillbranch.chat.models.data.User
import java.lang.NullPointerException


object FireBaseUtil {
    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )


    private val chatChannelsCollectionRef = fireStoreInstance.collection("chatChannels")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                        FirebaseAuth.getInstance().currentUser!!.uid, FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                        "", "")
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }

    fun addUser(user: User){
        val map = hashMapOf(
                "uid" to user.id,
                "firstName" to user.firstName,
                "lastName" to user.lastName,
                "lastVisit" to user.lastVisit,
                "email" to user.email

        )
        fireStoreInstance.collection("users")
                .add(map)
                .addOnSuccessListener { documentReference ->
                    print("Complete")
                }
                .addOnFailureListener { e ->
                    print("Error adding document")
                }
    }


    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }


    fun getUsers(): MutableList<User> {
        val items = mutableListOf<User>()
        fireStoreInstance.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        if (document.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(document.toObject(User::class.java)!!)
                    }
                }
        return items
    }


//    fun getUsers(onListen: (List<User>) -> Unit): ListenerRegistration {
//        return fireStoreInstance.collection("users")
//                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                    if (firebaseFirestoreException != null) {
//                        return@addSnapshotListener
//                    }
//
//                    val items = mutableListOf<User>()
//                    querySnapshot!!.documents.forEach {
//                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
//                            items.add(it.toObject(User::class.java)!!)
//                    }
//                    onListen(items)
//                }
//    }

    fun sendMessage(message: TextMessage, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
                .collection("messages")
                .add(message)
    }




}