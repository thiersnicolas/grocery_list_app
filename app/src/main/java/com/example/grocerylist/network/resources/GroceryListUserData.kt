package com.example.grocerylist.network.resources

import com.squareup.moshi.Json

data class GroceryListUserData(
    @Json(name = "userId")
    val id: String,
    @Json(name = "name")
    val name: String,
)