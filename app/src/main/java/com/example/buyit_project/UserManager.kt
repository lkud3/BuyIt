package com.example.buyit_project

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class UserManager {
    companion object{
        var users=ArrayList<User>()
        lateinit var currentUser:User

        fun authenticateUser(context: Context, inputUsername: String, inputPassword: String, authenticated:(User?) -> Unit){
            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Users.json"
            val requestQueue = Volley.newRequestQueue(context)
            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                {
                    response ->
                    users.clear()
                    val keys=response.keys()
                    keys.forEach {
                        key ->
                        val userObject = response.getJSONObject(key)
                        val nameDB = userObject.getString("username")
                        val passwordDB = userObject.getString("password")
                        val image = if (userObject.has("image")){
                            userObject.getString("image")
                        } else{
                            ""
                        }
                        val user=User(key, nameDB, passwordDB, image)

                        if (userObject.has("favorites")){
                            val favoriteIds=userObject.getJSONArray("favorites")
                            for (i in 0  until  favoriteIds.length()){
                                val favItem=StockManager.stock.find{
                                    it.id == favoriteIds.getString(i)
                                }
                                if (favItem != null) {
                                    user.favouriteItems.add(favItem)
                                }
                            }
                        }

                        users.add(user)

                    }

                    updateFriendsList(response)

                    val authenticatedUser=users.find {
                        it.password==inputPassword && it.username==inputUsername
                    }
                    authenticated(authenticatedUser)
                },
                {
                        error -> authenticated(null)
                }
            )
            requestQueue.add(jsonobjectrequest)
        }

        private fun updateFriendsList(response: JSONObject) {
            val keys = response.keys()
            keys.forEach { key ->
                val userObject = response.getJSONObject(key)
                if (userObject.has("friends")) {
                    val friendsIds = userObject.getJSONArray("friends")
                    val user = users.find {
                        it.id == key
                    }
                    if (user!= null) {
                        for (i in 0 until friendsIds.length()) {
                            val friendId = friendsIds.getString(i)
                            val friend = users.find {
                                it.id == friendId
                            }
                            if (friend!= null) {
                                user.friends.add(friend)
                            }
                        }
                    }
                }
            }
        }

        fun addUser(context: Context, username: String, password: String, userAdded:(User?) -> Unit){
            val image = "https://firebasestorage.googleapis.com/v0/b/cs300-buyit.appspot.com/o/login.png?alt=media&token=626c4fef-e45e-46b7-b847-c4a7b00cb044"
            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Users.json"
            val requestQueue = Volley.newRequestQueue(context)
            val userdata = JSONObject().apply{
                put("username", username)
                put("password", password)
                put("image", image)
            }
            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.POST,
                url,
                userdata,
                {
                        response ->
                    val id = response.getString("name")

                    val user=User(id, username, password, image)
                    users.add(user)

                    userAdded(user)
                },
                {
                        error -> userAdded(null)
                }
            )

            requestQueue.add(jsonobjectrequest)
        }


        fun addFavorite(context: Context, item: Stock, itemAdded: () -> Unit){

            if (currentUser.favouriteItems.contains(item)) {
                Toast.makeText(context, "Item already in favourites", Toast.LENGTH_SHORT).show()
                return
            }

            currentUser.favouriteItems.add(item)

            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Users/" + currentUser.id + ".json"
            val requestQueue = Volley.newRequestQueue(context)

            val favoriteItemsIds=ArrayList<String>()
            currentUser.favouriteItems.forEach{
                favoriteItemsIds.add(it.id)
            }

            val favoriteData=JSONObject().put("favorites", JSONArray(favoriteItemsIds))

            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.PATCH,
                url,
                favoriteData,
                {
                    response ->
                    itemAdded()
                },
                {
                        error ->
                        Toast.makeText(context, "Adding to the favourite failed", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonobjectrequest)
        }


        fun removeFavorite(context: Context, itemRemoved:() -> Unit){
            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Users/" + currentUser.id + ".json"
            val requestQueue = Volley.newRequestQueue(context)

            val favoriteItemsIds=ArrayList<String>()
            currentUser.favouriteItems.forEach{
                favoriteItemsIds.add(it.id)
            }

            val favoriteData=JSONObject().put("favorites", JSONArray(favoriteItemsIds))

            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.PATCH,
                url,
                favoriteData,
                {
                        response ->
                    itemRemoved()
                },
                {
                        error ->
                        Toast.makeText(context, "Removing from the favourite failed", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonobjectrequest)
        }

        fun addFriend(context: Context, friend: User, friendAdded: () -> Unit){

            if (currentUser.friends.contains(friend)) {
                Toast.makeText(context, "The user is already your friend.", Toast.LENGTH_SHORT).show()
                return
            }

            currentUser.friends.add(friend)

            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Users/" + currentUser.id + ".json"
            val requestQueue = Volley.newRequestQueue(context)

            val favoriteFriendsIds=ArrayList<String>()
            currentUser.friends.forEach{
                favoriteFriendsIds.add(it.id)
            }

            val friendsData=JSONObject().put("friends", JSONArray(favoriteFriendsIds))

            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.PATCH,
                url,
                friendsData,
                {
                        response ->
                    friendAdded()
                },
                {
                        error ->
                    Toast.makeText(context, "Adding to the friends failed", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonobjectrequest)
        }


        fun removeFriend(context: Context, itemRemoved:() -> Unit){
            val url = "https://cs300-buyit-default-rtdb.firebaseio.com/Users/" + currentUser.id + ".json"
            val requestQueue = Volley.newRequestQueue(context)

            val friendsIds=ArrayList<String>()
            currentUser.friends.forEach{
                friendsIds.add(it.id)
            }

            val friendData=JSONObject().put("friends", JSONArray(friendsIds))

            val jsonobjectrequest= JsonObjectRequest(
                Request.Method.PATCH,
                url,
                friendData,
                {
                        response ->
                    itemRemoved()
                },
                {
                        error ->
                    Toast.makeText(context, "Removing from friends failed", Toast.LENGTH_LONG).show()
                }
            )
            requestQueue.add(jsonobjectrequest)
        }

        fun getUserByUsername(username: String): User? {
            for (user in users) {
                if (user.username == username) {
                    return user
                }
            }
            return null
        }

    }
}