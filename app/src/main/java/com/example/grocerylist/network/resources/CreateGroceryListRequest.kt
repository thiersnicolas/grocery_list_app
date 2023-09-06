package com.example.grocerylist.network.resources

import com.squareup.moshi.Json

data class CreateGroceryListRequest(
    @Json(name = "name")
    val name: String
)