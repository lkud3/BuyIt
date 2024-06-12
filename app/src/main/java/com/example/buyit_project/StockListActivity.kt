package com.example.buyit_project

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import java.util.Locale

class StockListActivity : AppCompatActivity(), LocationListener {
    lateinit var locationManager: LocationManager
    lateinit var currentLocation: Location
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_stock_list)

        val rv=findViewById<RecyclerView>(R.id.rv)
        val sv=findViewById<SearchView>(R.id.searchView)
        val accDetails = findViewById<ShapeableImageView>(R.id.accountImage)
        val cart=findViewById<ShapeableImageView>(R.id.cartImage)
        val emptyText = findViewById<TextView>(R.id.empty_text)
        val locationSet = findViewById<TextView>(R.id.locationSet)
        locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager

        StockManager.getStock(this){
            val stockadapter = StockAdapter(applicationContext, StockManager.stock)
            rv.adapter = stockadapter
            val layoutManager = LinearLayoutManager(applicationContext)
            rv.layoutManager = layoutManager

            if (StockManager.stock.isEmpty()) {
                rv.visibility = View.GONE
                emptyText.visibility = View.VISIBLE
            } else {
                rv.visibility = View.VISIBLE
                emptyText.visibility = View.GONE
            }
        }

        accDetails.setOnClickListener{
            val intent = Intent(this, AccountDetails::class.java)
            startActivity(intent)
        }

        cart.setOnClickListener{
            val intent = Intent(this, UserCart::class.java)
            startActivity(intent)
        }

        locationSet.setOnClickListener {
            getCurrentLocation()
        }

        sv.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val filteredList=StockManager.stock.filter {
                        item -> item.name.contains(query.orEmpty(), ignoreCase = true)
                }
                val stockadapter = StockAdapter(applicationContext, filteredList as ArrayList<Stock>)
                rv.adapter=stockadapter

                Toast.makeText(applicationContext, query.toString(), Toast.LENGTH_LONG).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList=StockManager.stock.filter {
                        item -> item.name.contains(newText.orEmpty(), ignoreCase = true)
                }
                val stockadapter = StockAdapter(applicationContext, filteredList as ArrayList<Stock>)
                rv.adapter=stockadapter
                return true
            }
        })
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), 123)
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3000,
            1f,
            this
        )
    }

    override fun onLocationChanged(location: Location) {
        currentLocation = location
        filterStockByLocation()
    }


    private fun filterStockByLocation() {
        val emptyText = findViewById<TextView>(R.id.empty_text)
        val rv=findViewById<RecyclerView>(R.id.rv)
        val currentCity = getCurrentCity(currentLocation)
        val filteredStock = StockManager.stock.filter { it.location == currentCity }
        Toast.makeText(this, "The list is filtered for Mountain View location,", Toast.LENGTH_LONG).show()
        val stockAdapter = StockAdapter(applicationContext, filteredStock as ArrayList<Stock>)
        rv.adapter = stockAdapter
        val layoutManager = LinearLayoutManager(applicationContext)
        rv.layoutManager = layoutManager

        if (StockManager.stock.isEmpty()) {
            rv.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }
    }

    private fun getCurrentCity(location: Location): String {
        return getCityFromLocation(location)
    }

    private fun getCityFromLocation(location: Location): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        return addresses!![0].locality
    }
}