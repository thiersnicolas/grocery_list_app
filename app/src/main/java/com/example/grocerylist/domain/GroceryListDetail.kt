package com.example.grocerylist.domain

import java.time.LocalDateTime
import java.util.*

data class GroceryListDetail(
    val id: UUID,
    val name: String,
    val createdBy: User,
    val createdDate: LocalDateTime,
    val status: String,
    val collectBy: User?,
    val currency: String?,
    val price: Int?
)
