package com.example.mm_inventory.model.repo.user

import android.util.Log
import com.example.mm_inventory.model.data.response.Response
import com.example.mm_inventory.model.data.user.UserState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.reflect.Executable

class UserRepo {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val USER_COLLECTION = "users"
    private val EMAIL_USER_FIELD = "email"

    suspend fun verifyUserCredentials(email: String, password: String): Response<UserState> {
        return try {
            val user = db.collection(USER_COLLECTION)
                .whereEqualTo(EMAIL_USER_FIELD, email.lowercase().trim())
                .get()
                .await()
                .first()
                .toObject(UserState::class.java)
            if (user.password == password) Response.Success(data = user)
            else Response.Error(e = "incorrect password")
        } catch (e: Exception) {
            Response.Error(e = "... user dont exists")
        }
    }

    //add user to firestore
    suspend fun addUser(userState: UserState): Response<Boolean> {
        return try {
            val newRef = db.collection(USER_COLLECTION).document()
            newRef.set(userState.copy(idUser = newRef.id)).await()
            Response.Success(true)
        } catch (e: Exception) {
            Response.Error(e.toString())
        }
    }

    fun isUserAuthenticated() = auth.currentUser != null
    fun userOut() = auth.signOut()

    fun getFirebaseAuthState() = callbackFlow  {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    //add user to authentication
    suspend fun addUserTest(email: String, password: String): Response<String> {
        return try{
            val user = auth.createUserWithEmailAndPassword(email, password).await()
            if (user.user != null) Response.Success(user.user!!.email!!)
            else Response.Error("user dont registered")
        } catch (e: Exception){
            Response.Error(e.toString())
        }
    }

    suspend fun signIn(email: String, password: String): Response<FirebaseUser>{
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await()
            Response.Success(user.user!!)
        } catch (e: Exception){
            Response.Error(e.toString())
        }
    }



}




















