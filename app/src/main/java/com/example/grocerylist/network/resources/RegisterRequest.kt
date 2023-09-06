package com.example.grocerylist.model.network.rest.resources

import com.squareup.moshi.Json

data class RegisterRequest(
    @Json(name = "name")
    val name: String,

    @Json(name = "email")
    val email: String,

    @Json(name = "password")
    val password: String,
)
