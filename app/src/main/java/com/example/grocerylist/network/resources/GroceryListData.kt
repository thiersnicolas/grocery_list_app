package com.example.grocerylist.network.resources

import com.squareup.moshi.Json

data class GroceryListData(
    @Json(name = "groceryListId")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "createdBy")
    val createdBy: String,
    @Json(name = "createdDate")
    val createdDate: String,
    @Json(name = "status")
    val status: String,
    @Json(name = "collectBy")
    val collectBy: String,
)