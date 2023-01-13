package com.example.thingsisee.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.thingsi.Data.Post
import com.example.thingsisee.Data.User
import com.google.firebase.database.*

class PostRepository {

    val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")
    val userDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    @Volatile private var INSTANCE: PostRepository? = null

    fun getInstance(): PostRepository {
        return INSTANCE ?: synchronized(this) {
            val instance = PostRepository()
            INSTANCE = instance
            instance
        }
    }

    fun loadPosts(postList: MutableLiveData<List<Post>>) {

        dbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                try {
                    val _postList: List<Post> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Post::class.java)!!
                    }.sortedByDescending { it.timestamp }
                    postList.postValue(_postList)
                }catch (e: Exception) {
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun deletePost(post: Post) {
        dbRef.child(post.postId!!).removeValue()
    }

    fun loadComments(postId: String, commentList: MutableLiveData<List<Post>>) {
        dbRef.child(postId).child("Comments").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _commentList: List<Post> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Post::class.java)!!
                    }
                    commentList.postValue(_commentList)
                } catch (e: Exception) {}
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun loadUserPics(picList: MutableLiveData<List<User>>) {
        userDbRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _picList: List<User> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(User::class.java)!!
                    }
                    picList.postValue(_picList)
                }
catch (e: Exception) { }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}