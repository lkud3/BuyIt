package com.example.buyit_project

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class AddFriend : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_friend)

        val accDetails = findViewById<ShapeableImageView>(R.id.accountImage)
        val cart=findViewById<ShapeableImageView>(R.id.cartImage)
        val goBack = findViewById<ImageView>(R.id.goBack)
        val rv=findViewById<RecyclerView>(R.id.rv)

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

        val friendsAdapter = AddFriendAdapter(this, UserManager.users)
        rv.adapter = friendsAdapter
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager
    }
}