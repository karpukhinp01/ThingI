package com.example.thingsisee.Repository

import androidx.lifecycle.MutableLiveData
import com.example.thingsi.Data.Post
import com.google.firebase.database.*

class PostRepository {

    val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("Posts")

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
                    }

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

}