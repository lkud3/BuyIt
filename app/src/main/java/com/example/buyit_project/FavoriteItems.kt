package com.example.buyit_project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class FavoriteItems : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorite_items)

        val accDetails = findViewById<ShapeableImageView>(R.id.accountImage)
        val cart=findViewById<ShapeableImageView>(R.id.cartImage)
        val goBack = findViewById<ImageView>(R.id.goBack)
        val rv = findViewById<RecyclerView>(R.id.rv)
        val userName=intent.getStringExtra("username")
        val user = UserManager.getUserByUsername(userName.toString())
        val emptyText = findViewById<TextView>(R.id.empty_text)

        findViewById<TextView>(R.id.favText).text = when(userName){
            UserManager.currentUser.username -> "Your Favorites"
            else -> "$userName Favorites"
        }


        accDetails.setOnClickListener{
            val intent = Intent(this, AccountDetails::class.java)
            startActivity(intent)
        }

        cart.setOnClickListener{
            val intent = Intent(this, UserCart::class.java)
            startActivity(intent)
        }

        goBack.setOnClickListener{
            finish()
        }

        val favAdapter = if (userName == UserManager.currentUser.username){
            FavoritesAdapter(this, UserManager.currentUser.favouriteItems)
        } else{
            FriendFavAdapter(this, user!!.favouriteItems)
        }

        rv.adapter = favAdapter
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        if (UserManager.currentUser.username.isEmpty()) {
            rv.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }
    }
}