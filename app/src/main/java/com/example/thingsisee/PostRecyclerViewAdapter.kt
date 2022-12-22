package com.example.thingsisee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.thingsi.Data.Post
import com.example.thingsisee.Data.PostViewModel

class PostRecyclerViewAdapter: RecyclerView.Adapter<PostRecyclerViewAdapter.ViewHolder>() {

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
        val heading: TextView = itemView.findViewById(R.id.heading)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}
