package com.example.thingsisee.ViewModels

import android.annotation.SuppressLint
import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thingsi.Data.Post
import com.example.thingsisee.Event
import com.example.thingsisee.FirstFragment
import com.example.thingsisee.Repository.PostRepository
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class MainViewModel() : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("Posts")

    private val repository: PostRepository
    private val _allPosts = MutableLiveData<List<Post>>()
    val allPosts: LiveData<List<Post>> get() = _allPosts

    init {
        repository = PostRepository().getInstance()
        repository.loadPosts(_allPosts)
    }

    private val _statusMessage = MutableLiveData<Event<String>>()

    val statusMessage : LiveData<Event<String>>
        get() = _statusMessage



    @SuppressLint("SuspiciousIndentation")
    fun insertData(name: String, text: String) {
        val postId = dbRef.push().key!!
        dbRef.child(postId).setValue(Post(postId, name, text)).addOnCompleteListener {
            _statusMessage.value = Event("Posted successfully!")
        }
            .addOnCanceledListener { _statusMessage.value = Event("Error!") }
    }
}