package com.shkiper.chat.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.shkiper.chat.models.data.User
import java.util.*

object FireBaseUtils {

    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val currentUserDocRef: DocumentReference
        get() = fireStoreInstance.document(
                "users/${FirebaseAuth.getInstance().currentUser?.uid
                        ?: throw NullPointerException("UID is null.")}"
        )

    fun initCurrentUserIfFirstTime() {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                with(FirebaseAuth.getInstance().currentUser){
                    val newUser = User(this!!.uid, displayName ?: "unknown", "", email = email ?: "")
                    currentUserDocRef.set(newUser)
                }
            }
        }
    }

    fun updateCurrentUser(date: Date, online: Boolean) {
        val userFieldMap = mutableMapOf<String, Any>()
        userFieldMap["lastVisit"] = date
        userFieldMap["online"] = online
        currentUserDocRef.update(userFieldMap)
    }



    fun getUnreadMessages(chatId: String): Int {
//        val result = mutableListOf<BaseMessage>()
//        chatsCollectionRef.document(chatId).collection("messages")
//                .get()
//                .addOnSuccessListener { querySnapshot ->
//                    querySnapshot.documents.forEach {
//                        val message = it.toObject(TextMessage::class.java)
//                        if (message != null) {
//                            result.add(message)
//                        }
//                        return@forEach
//                    }
//                }
//        return result.filter { !it.isRead }.size
        return 1
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }

}