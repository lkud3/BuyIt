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

class UserCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_cart)

        val accDetails = findViewById<ShapeableImageView>(R.id.accountImage)
        val cart=findViewById<ShapeableImageView>(R.id.cartImage)
        val goBack = findViewById<ImageView>(R.id.goBack)
        val rv = findViewById<RecyclerView>(R.id.rv)
        val totalPrice = findViewById<TextView>(R.id.totalPrice)
        val emptyText = findViewById<TextView>(R.id.empty_text)

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

        val cartAdapter = CartAdapter(this, StockManager.cart)
        rv.adapter = cartAdapter
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        if (StockManager.cart.isEmpty()) {
            rv.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }

        total_price_calc(totalPrice)

        cartAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                total_price_calc(totalPrice)
            }
        })
    }

    private fun total_price_calc(totalPrice:TextView){
        val total: Double = StockManager.cart.sumOf { it.price }
        totalPrice.text = "Total Price: $total€"
    }
}