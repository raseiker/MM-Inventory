package com.example.mm_inventory.ui.viewModel.user

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mm_inventory.model.data.response.Response
import com.example.mm_inventory.model.data.user.UserState
import com.example.mm_inventory.model.repo.user.UserRepo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val repo = UserRepo()
    private var _user = MutableStateFlow(UserState())
    val user = _user.asStateFlow()
    var confirmPass = mutableStateOf(value = "")
    val isUserAutheticated = mutableStateOf(repo.isUserAuthenticated())
    var isLoading = mutableStateOf(value = true)
        private set

    fun onValueChanged(field: String, value: Any) {
        when (field) {
            "name" -> _user.update { it.copy(name = value.toString()) }
            "email" -> _user.update { it.copy(email = value.toString()) }
            "password" -> _user.update { it.copy(password = value.toString()) }
            "confirmPass" -> confirmPass.value = value.toString()
        }
    }

    fun onClearText(field: String) {
        when (field) {
            "name" -> _user.update { it.copy(name = "") }
            "email" -> _user.update { it.copy(email = "") }
            "password" -> _user.update { it.copy(password = "") }
            "confirmPass" -> confirmPass.value = ""
            "all" -> _user.update { UserState() }
        }
    }

    fun createUser() {
        viewModelScope.launch {
            db.collection("users")
                .add(_user.value)
                .addOnSuccessListener { docRef ->
                    Log.d("addUser", "DocumentSnapshot added with ID: ${docRef.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("addUser", "Error", e)
                }
        }
    }

    fun verifyUser() {
        viewModelScope.launch {
            db.collection("users")
                .get()
                .addOnSuccessListener { result ->
                    val loggedUser =
                        result.documents.find { it["email"] == _user.value.email.lowercase().trim() }
                    if (loggedUser == null){
                        Log.d("verifyUser", "Email dont exist !!")
                    } else {
                        val user = loggedUser.toObject<UserState>() as UserState
                        if (user.password == _user.value.password){
                            Log.d("verifyUser", "Hi ${user.name}")
                        } else {
                            Log.d("verifyUser", "incorrect password")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("verifyUser", "Error", e)
                }
        }
    }

    fun verifyUserCredentials() = viewModelScope.launch {
        when (val res = repo.verifyUserCredentials(email = _user.value.email, password = _user.value.password)){
            is Response.Error -> Log.d("verifyUser", res.e)
            Response.Loading -> isLoading.value = true
            is Response.Success -> Log.d("verifyUser", "Hi ${res.data.name}").also { isLoading.value =  false }
        }
    }

    fun addUser() = viewModelScope.launch {
        when (val res = repo.addUser(_user.value)){
            is Response.Error -> Log.w("addUser", "Error..")
            Response.Loading -> Log.d("addUser", "loading...")
            is Response.Success -> Log.d("addUser", "user added? : ${res.data}")
        }
    }

    fun addUserTest() = viewModelScope.launch {
        when (val res = repo.addUserTest(email = _user.value.email, password = _user.value.password)){
            is Response.Error -> Log.w("addUser", "Error.. ${res.e}" )
            Response.Loading -> Log.d("addUser", "loading...")
            is Response.Success -> Log.d("addUser", "user added with email..? : ${res.data}").also{ isLoading.value = false}
        }
    }

    fun userOut(): UserViewModel { repo.userOut().also { changeAuthState(); return this} }

    fun signIn() = viewModelScope.launch {
        when (val res = repo.signIn(email = _user.value.email, password = _user.value.password)){
            is Response.Error -> Log.d("verifyUser", "Error.. ${res.e}" )
            Response.Loading -> Log.d("verifyUser", "loading...")
            is Response.Success -> Log.d("verifyUser", "user added with email..? : ${res.data}").also { changeAuthState() }
        }
    }

    private fun changeAuthState () = viewModelScope.launch {
        repo.getFirebaseAuthState().collect{
            isUserAutheticated.value = it
        }
    }

    fun navigateInApp(routeFrom: String, routeTo: String) = if (isUserAutheticated.value) routeFrom else routeTo
    fun navigateOutApp(routeTo: String) = if (!isUserAutheticated.value) routeTo else null
}












