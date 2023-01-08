package com.example.thingsisee.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getDrawableOrThrow
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.thingsi.Data.Post
import com.example.thingsisee.Data.AuthState
import com.example.thingsisee.Data.Event
import com.example.thingsisee.Data.FirebaseUserLiveData
import com.example.thingsisee.Data.LoadStatus
import com.example.thingsisee.Repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.*

class MainViewModel() : ViewModel() {

    private var auth = FirebaseAuth.getInstance()
    val user = Firebase.auth.currentUser
    var _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus> get() = _status

    var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    fun setStatus(status: LoadStatus) {
        _status.value = status
    }
    fun setStatusMessage() {
        if (status.value == LoadStatus.PENDING) _statusMessage.value = "Getting there..!"
        else _statusMessage.value = "bfgxdz"
    }



    fun register(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnFailureListener { exception ->
            Log.d("registration error", exception.toString())
        }.addOnSuccessListener { _status.value = LoadStatus.REGISTERED }
    }

    fun login(email: String, password: String) {

        auth.signInWithEmailAndPassword(
            email, password
        ).addOnFailureListener { exception ->
            Log.d("login error", exception.toString())
        }
    }

    fun addUserInfo(name: String) {
        val profileUpdate = userProfileChangeRequest {
            displayName = name
        }
        user!!.updateProfile(profileUpdate).addOnSuccessListener { _status.value = LoadStatus.SUCCESS }
            .addOnFailureListener { Log.d("r", "no way") }
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

    private val dbRef = FirebaseDatabase.getInstance().getReference("Posts")

    private val repository: PostRepository
    private val _allPosts = MutableLiveData<List<Post>>()
    val allPosts: LiveData<List<Post>> get() = _allPosts

    init {
        repository = PostRepository().getInstance()
        repository.loadPosts(_allPosts)
    }

    fun deletePost(post: Post) {
        repository.deletePost(post)
    }


    fun insertData(name: String, text: String) {
        val postId = dbRef.push().key!!
        Log.d("ins", "trying to insert")
        dbRef.child(postId).setValue(Post(postId, name, text)).addOnCompleteListener {
            _status.value = LoadStatus.SUCCESS
        }
            .addOnCanceledListener { _status.value = LoadStatus.FAILURE }

    }

    val authState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthState.AUTHENTIFICATED
        } else {
            AuthState.UNAUTHENTIFICATED
        }
    }
    fun resetStatus() {
        _status.value = LoadStatus.OK
    }

}