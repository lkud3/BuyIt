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

class CartAdapter (private  val context: Context, private  val items:ArrayList<Stock>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder> () {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        return object: RecyclerView.ViewHolder(view){}
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item=items[position]
        val imgView=holder.itemView.findViewById<ImageView>(R.id.imageViewItem)

        Picasso.get()
            .load(item.image)
            .error(R.drawable.ic_launcher_background)
            .placeholder(R.drawable.loading)
            .into(imgView)
        holder.itemView.findViewById<TextView>(R.id.textViewItemName).text=item.name
        holder.itemView.findViewById<TextView>(R.id.price).text= "${item.price}€"

        holder.itemView.findViewById<TextView>(R.id.textViewItemRemove).setOnClickListener {
            StockManager.cart.remove(item)
            Toast.makeText(context,"Item Removed", Toast.LENGTH_LONG).show()
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            val intent= Intent(context, ItemInfo::class.java).apply {
                putExtra("name", item.name)
                putExtra("desc", item.description)
                putExtra("image", item.image)
                putExtra("price", item.price)
                putExtra("id", item.id)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }
}