package com.example.thingsisee.ViewModels


import android.content.Intent

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.thingsi.Data.Post
import com.example.thingsisee.Data.AuthState
import com.example.thingsisee.Data.FirebaseUserLiveData
import com.example.thingsisee.Data.LoadStatus
import com.example.thingsisee.Data.User
import com.example.thingsisee.Repository.PostRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class MainViewModel() : ViewModel() {

    private var auth = FirebaseAuth.getInstance()
    val user = Firebase.auth.currentUser
    private var _profilePicUrl = MutableLiveData<Uri>()
    val profilePicUrl: LiveData<Uri> get() = _profilePicUrl

    fun updateUri() {
        _profilePicUrl.value = user!!.photoUrl
    }

    var _status = MutableLiveData<LoadStatus>()
    val status: LiveData<LoadStatus> get() = _status

    var _statusMessage = MutableLiveData<String>()
    val statusMessage: LiveData<String> get() = _statusMessage

    fun setStatus(status: LoadStatus) {
        _status.value = status
    }

    private var _allUserPics = MutableLiveData<List<User>>()
    val allUserPics: LiveData<List<User>> get() = _allUserPics



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
    val userdb = FirebaseDatabase.getInstance().getReference("Users")

    private val repository: PostRepository
    private val _allPosts = MutableLiveData<List<Post>>()
    val allPosts: LiveData<List<Post>> get() = _allPosts

    init {
        repository = PostRepository().getInstance()
        repository.loadPosts(_allPosts)
        repository.loadUserPics(_allUserPics)
    }

    fun deletePost(post: Post) {
        repository.deletePost(post)
    }

    fun getUserPosts(name: String): LiveData<List<Post>> {
        return allPosts.map {
            it.filter {
                it.postName.equals(name)
            }
        }
    }

    fun insertData(name: String, text: String) {
        val postId = dbRef.push().key!!
        val timestamp = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        Log.d("ins", "trying to insert")
        dbRef.child(postId).setValue(Post(postId, timestamp, user!!.uid, name, text)).addOnCompleteListener {
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

    var selectedUri: Uri? = null

    fun uploadUserPic(startForResult: ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        Log.d("pic", "openactivityforresult")
        startForResult.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
        Log.d("picture", selectedUri.toString())
    }
    fun uploadUserPicToFirebaseStorage() {
        Log.d("pic", "uploaduserpic started")
        if (selectedUri == null) {
            return
        }

        val filename = UUID.randomUUID().toString()
        val storageRef = FirebaseStorage.getInstance().getReference("/profilePics/${filename}")
        storageRef.putFile(selectedUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                val profilePicUpdate = userProfileChangeRequest {
                    photoUri = it
                    userdb.child(user!!.uid).setValue(User(user.uid, it.toString()))
                }
                user!!.updateProfile(profilePicUpdate).addOnSuccessListener {
                    Log.d("pic", "set successfully")
                    updateUri()
                }

            }
        }
    }

}