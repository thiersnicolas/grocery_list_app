package com.example.grocerylist.domain

import java.util.*


data class AppUser(
    val id: UUID,
    val name: String,
    val email: String,
)