package com.example.buyit_project

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class AddFriendAdapter(private val context: Context, private val friends: ArrayList<User>) : RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.add_friend_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return friends.size-1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = friends[position]

        if (friend != UserManager.currentUser &&!UserManager.currentUser.friends.contains(friend)) {
            holder.bind(friend)
        } else {
            holder.itemView.visibility = View.GONE
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgView: ImageView = itemView.findViewById(R.id.imageViewFriend)
        private val textViewFriendName: TextView = itemView.findViewById(R.id.textViewFriendName)
        private val textViewFriendAdd: TextView = itemView.findViewById(R.id.textViewFriendAdd)

        fun bind(friend: User) {

            Picasso.get()
                .load(friend.image)
                .error(R.drawable.login)
                .placeholder(R.drawable.loading)
                .into(imgView)

            textViewFriendName.text = friend.username

            textViewFriendAdd.setOnClickListener {
                UserManager.addFriend(context, friend) {
                    Toast.makeText(context, "Friend Added", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}