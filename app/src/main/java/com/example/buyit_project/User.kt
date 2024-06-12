package com.example.buyit_project

class User (val id: String, val username: String, val password: String, val image: String){
    val favouriteItems=ArrayList<Stock>()
    val friends = ArrayList<User>()
}