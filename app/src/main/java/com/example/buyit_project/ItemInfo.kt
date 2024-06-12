package com.example.buyit_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class ItemInfo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_item_info)

        val imgItem=findViewById<ImageView>(R.id.itemImage)
        val nameItem=findViewById<TextView>(R.id.itemName)
        val descItem=findViewById<TextView>(R.id.itemDesc)
        val priceItem=findViewById<TextView>(R.id.itemPrice)
        val accDetails = findViewById<ShapeableImageView>(R.id.accountImage)
        val cart=findViewById<ShapeableImageView>(R.id.cartImage)
        val addCart = findViewById<Button>(R.id.addCart)
        val addFav = findViewById<Button>(R.id.addFav)
        val goBack = findViewById<ImageView>(R.id.goBack)

        Picasso.get()
            .load(intent.getStringExtra("image"))
            .error(R.drawable.ic_launcher_background)
            .placeholder(R.drawable.loading)
            .into(imgItem)

        nameItem.text=intent.getStringExtra("name")
        descItem.text=intent.getStringExtra("desc")
        priceItem.text= "Price: ${intent.getDoubleExtra("price", 0.0)}€"
        val itemId=intent.getStringExtra("id")

        val item=StockManager.stock.find{
            it.id == itemId
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

        addFav.setOnClickListener{
            if (item != null) {
                UserManager.addFavorite(this, item){
                    Toast.makeText(this,"Item Added", Toast.LENGTH_LONG).show()
                }
            }
        }

        addCart.setOnClickListener {
            if (StockManager.cart.contains(item)) {
                Toast.makeText(this, "Item is already in your cart.", Toast.LENGTH_SHORT).show()
            } else {
                if (item != null) {
                    StockManager.cart.add(item)
                    Toast.makeText(this, "Item Added", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}