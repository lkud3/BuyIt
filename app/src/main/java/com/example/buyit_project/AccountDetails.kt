package com.example.buyit_project

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.hardware.SensorEventListener
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class AccountDetails : AppCompatActivity(), SensorEventListener {
    lateinit var txtTemperature: TextView
    lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_account_details)

        val imgUser=findViewById<ShapeableImageView>(R.id.userPicture)
        val nameUser=findViewById<TextView>(R.id.userName)
        val rv=findViewById<RecyclerView>(R.id.rvFriends)
        val btSavedItems=findViewById<Button>(R.id.buttonSavedItems)
        val accDetails = findViewById<ShapeableImageView>(R.id.accountImage)
        val cart=findViewById<ShapeableImageView>(R.id.cartImage)
        val goBack = findViewById<ImageView>(R.id.goBack)
        val addFriend = findViewById<TextView>(R.id.addFriend)
        val emptyText = findViewById<TextView>(R.id.empty_text)

        txtTemperature=findViewById(R.id.textTemp)
        sensorManager=getSystemService(SENSOR_SERVICE) as SensorManager
        temperatureSensor= sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)!!



        Picasso.get()
            .load(UserManager.currentUser.image)
            .error(R.drawable.login)
            .placeholder(R.drawable.loading)
            .into(imgUser)

        nameUser.text="Welcome ${UserManager.currentUser.username}"

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


        val friendsAdapter = FriendAdapter(this, UserManager.currentUser.friends)
        rv.adapter = friendsAdapter
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        if (UserManager.currentUser.friends.isEmpty()) {
            rv.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        }

        btSavedItems.setOnClickListener {
            val intent = Intent(this, FavoriteItems::class.java).apply{
                putExtra("username", UserManager.currentUser.username)
            }
            startActivity(intent)
        }

        addFriend.setOnClickListener {
            val intent = Intent(this, AddFriend::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        val rv=findViewById<RecyclerView>(R.id.rvFriends)
        val friendsAdapter = FriendAdapter(this, UserManager.currentUser.friends)
        rv.adapter = friendsAdapter
        val layoutManager = LinearLayoutManager(this)
        rv.layoutManager = layoutManager

        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause(){
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_AMBIENT_TEMPERATURE){
            val temp = event.values[0]
            txtTemperature.text="Current temperature is: ${Math.round(temp)}°"
        }

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }


}