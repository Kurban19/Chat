package ru.skillbranch.chat.data.managers

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
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
                        "")
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }


    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
                .addOnSuccessListener {
                    onComplete(it.toObject(User::class.java)!!)
                }
    }


    fun getUsers(onListen: (List<User>) -> Unit): ListenerRegistration {
        return fireStoreInstance.collection("users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        return@addSnapshotListener
                    }

                    val items = mutableListOf<User>()
                    querySnapshot!!.documents.forEach {
                        if (it.id != FirebaseAuth.getInstance().currentUser?.uid)
                            items.add(it.toObject(User::class.java)!!)
                    }
                    onListen(items)
                }
    }




}