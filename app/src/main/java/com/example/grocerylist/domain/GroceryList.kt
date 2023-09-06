package com.example.grocerylist.domain

import java.time.LocalDateTime
import java.util.*

data class GroceryList(
    val id: UUID,
    val name: String,
    val createdBy: UUID,
    val createdDate: LocalDateTime,
    val status: String
)
