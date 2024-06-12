package com.example.buyit_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class RegisterUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_regester_user)

        val editTextName = findViewById<TextInputEditText>(R.id.etNewUsername)
        val editTextPassword = findViewById<TextInputEditText>(R.id.etNewPassword)
        val registerButton = findViewById<Button>(R.id.newUserButton)

        registerButton.setOnClickListener {
            UserManager.addUser(this, editTextName.text.toString(), editTextPassword.text.toString()){
                    response ->
                if(response != null){
                    UserManager.currentUser=response
                    val intent = Intent(this, StockListActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Addition failed, handle accordingly", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}