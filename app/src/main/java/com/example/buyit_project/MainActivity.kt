package com.example.buyit_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val editTextName = findViewById<TextInputEditText>(R.id.etUsername)
        val editTextPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<TextView>(R.id.registerButton)

        StockManager.getStock(this){

        }

        loginButton.setOnClickListener {
            UserManager.authenticateUser(this, editTextName.text.toString(), editTextPassword.text.toString()){
                response ->
                if(response != null){
                    UserManager.currentUser=response
                    val intent = Intent(this, StockListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Authentication failed, handle accordingly", Toast.LENGTH_LONG).show()
                }
            }
        }

        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterUser::class.java)
            startActivity(intent)
        }
    }
}