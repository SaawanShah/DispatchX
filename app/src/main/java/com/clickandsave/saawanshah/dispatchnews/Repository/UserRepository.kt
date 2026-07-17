package com.clickandsave.saawanshah.dispatchnews.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth= FirebaseAuth.getInstance()
    private val firestore= FirebaseFirestore.getInstance()

    suspend fun signUp(name: String, email: String, password: String, phone: String): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user!!.uid
            val userData = hashMapOf(
                "uid" to uid,
                "name" to name,
                "email" to email,
                "phone" to phone
            )
            firestore.collection("Users").document(uid).set(userData).await()
            Result.success("User registered successfully")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}