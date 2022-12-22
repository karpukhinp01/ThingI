package com.example.thingsisee.ViewModels

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thingsi.Data.Post
import com.example.thingsisee.FirstFragment
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class MainViewModel() : ViewModel() {
    private val dbRef = FirebaseDatabase.getInstance().getReference("Posts")

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
    get() = _status

    fun resetStatus() {
        Timer().schedule(2000) {
            _status.postValue("")
        }
    }

    fun insertData(name: String, text: String) {
        val postId = dbRef.push().key!!
        dbRef.child(postId).setValue(Post(postId, name, text)).addOnCompleteListener {
        _status.value = "Success!"
            resetStatus()
        }
            .addOnFailureListener { _status.value = "Error!" }
    }
}