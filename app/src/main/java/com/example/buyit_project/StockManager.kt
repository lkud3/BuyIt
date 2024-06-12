package com.example.buyit_project

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class StockManager {
    companion object{
        var stock=ArrayList<Stock>()
        var cart=ArrayList<Stock>()

        fun getStock(context: Context, datafetched:() -> Unit){
            stock.clear()

            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Stock.json"

            val requestQueue = Volley.newRequestQueue(context)
            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                {
                    response ->
                    val keys = response.keys()
                    keys.forEach {
                            key ->
                        val stockObject = response.getJSONObject(key)
                        val name = stockObject.getString("name")
                        val image = stockObject.getString("image")
                        val location = stockObject.getString("location")
                        val price = stockObject.getDouble("price")
                        val descritpion = stockObject.getString("description")
                        val item=Stock(key, name, image, location, price, descritpion)
                        stock.add(item)
                    }
                    datafetched()
                },
                {
                        error ->
                        Toast.makeText(context, "Stock update failed.", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonobjectrequest)
        }
    }
}