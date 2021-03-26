package com.shkiper.chat.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.shkiper.chat.models.data.User
import java.util.*

//object FireBaseUsers {
//    private val fireStoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
//
//    private const val TAG = "FireBase"
//
//    private val currentUserDocRef: DocumentReference
//        get() = fireStoreInstance.document(
//            "users/${
//                FirebaseAuth.getInstance().currentUser?.uid
//                ?: throw NullPointerException("UID is null.")}"
//        )
//
//    private val usersCollectionRef = fireStoreInstance.collection("users")
//

//    fun getUsers(){
//        usersCollectionRef
//            .get()
//            .addOnSuccessListener { result ->
//                Log.d(TAG, result.documents.size.toString())
//                for (document in result) {
//                    if (document.id != FirebaseAuth.getInstance().currentUser!!.uid)
////                        if(UsersRepository.findUser(document.id) == null){
////                            UsersRepository.addUser(document.toObject(User::class.java))
////                        }
//                }
//            }
//    }
//    }


//    fun initCurrentUserIfFirstTime() {
//        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
//            if (!documentSnapshot.exists()) {
//                with(FirebaseAuth.getInstance().currentUser){
//                    val newUser = User(this!!.uid, displayName ?: "unknown", "", email = email ?: "")
//                    currentUserDocRef.set(newUser)
//                }
//            }
//        }
//    }
//
//    fun updateCurrentUser(date: Date = Date(), online: Boolean) {
//        val userFieldMap = mutableMapOf<String, Any>()
//        userFieldMap["lastVisit"] = date
//        userFieldMap["online"] = online
//        currentUserDocRef.update(userFieldMap)
//    }

