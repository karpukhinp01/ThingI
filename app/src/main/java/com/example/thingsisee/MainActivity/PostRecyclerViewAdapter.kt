package com.example.thingsisee.MainActivity

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.thingsi.Data.Post
import com.example.thingsisee.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class PostRecyclerViewAdapter(val start: Int, context: Context): RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {

    val context = context
    private val mList = ArrayList<Post>()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val currentItem = mList[position]

        holder.itemView.setOnClickListener {
            val action1 = PostFragmentDirections.actionPostFragmentToEditFragment(currentItem)
            val action2 = UserProfileFragmentDirections.actionUserProfileFragmentToEditFragment(currentItem)
            if (start == 1) {
                holder.itemView.findNavController().navigate(action1)
            } else {
                holder.itemView.findNavController().navigate(action2)
            }
        }


        Picasso.with(context).load(currentItem.userPic).into(holder.userPicture)
        // sets the image to the imageview from our itemHolder class
        holder.heading.text = currentItem.postName

        // sets the text to the textview from our itemHolder class
        holder.textView.text = currentItem.postText
    }


    fun updatePosts(postList: List<Post>) {
        this.mList.clear()
        this.mList.addAll(postList)
        notifyDataSetChanged()

    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val userPicture: CircleImageView = itemView.findViewById(R.id.card_profile_image)
        val heading: TextView = itemView.findViewById(R.id.heading)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}
