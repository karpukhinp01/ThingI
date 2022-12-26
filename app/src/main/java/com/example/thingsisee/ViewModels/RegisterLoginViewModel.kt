package com.example.thingsisee.ViewModels

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thingsisee.Data.Event
import com.google.firebase.auth.FirebaseAuth

class RegisterLoginViewModel: ViewModel() {

    private var auth = FirebaseAuth.getInstance()

    private val _statusMessage = MutableLiveData<Event<String>>()
    val statusMessage: LiveData<Event<String>>
        get() = _statusMessage

    fun register(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _statusMessage.value = Event("Successfully registered!")
            }
        }.addOnFailureListener { exception ->
            _statusMessage.value = Event(exception.localizedMessage) as Event<String>?
        }
    }

    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                _statusMessage.value = Event("Successfully logged in!")
            }
        }.addOnFailureListener { exception ->
            _statusMessage.value = Event(exception.localizedMessage) as Event<String>?
        }
    }

    fun checkInputs(email: String, password: String): Boolean {
        return !(email.isEmpty() || password.isEmpty())
    }

    fun checkPassword(password: String): String {
        return when {
            password.length < 6 -> "Your password should contain at least 6 symbols"
            password.isDigitsOnly() -> "Your password should contain at least 1 letter"
            else -> ""
        }
    }
}