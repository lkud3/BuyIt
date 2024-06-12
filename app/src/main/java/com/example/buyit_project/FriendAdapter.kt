package com.example.buyit_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FriendAdapter (private  val context: Context, private  val friends:ArrayList<User>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_item_layout, parent, false)
        return object: RecyclerView.ViewHolder(view){}
    }

    override fun getItemCount(): Int {
        return friends.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val friend=friends[position]
        val imgView=holder.itemView.findViewById<ImageView>(R.id.imageViewFriend)

        Picasso.get()
            .load(friend.image)
            .error(R.drawable.login)
            .placeholder(R.drawable.loading)
            .into(imgView)

        holder.itemView.findViewById<TextView>(R.id.textViewFriendName).text=friend.username

        holder.itemView.findViewById<TextView>(R.id.textViewFriendRemove).setOnClickListener {
            UserManager.currentUser.friends.remove(friend)
            notifyDataSetChanged()
            UserManager.removeFriend(context){
                Toast.makeText(context,"Friend Removed", Toast.LENGTH_LONG).show()
            }

        }

        holder.itemView.setOnClickListener {
            val intent= Intent(context, FavoriteItems::class.java).apply {
                putExtra("username", friend.username)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}