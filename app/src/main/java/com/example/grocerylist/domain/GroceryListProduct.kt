package com.example.grocerylist.domain

import java.util.*

data class GroceryListProduct(
    val id: UUID,
    val groceryListId: UUID,
    val name: String,
    val amount: Int?,
    val unit: String?,
    val addedBy: User,
    val remark: String?,
    val updatedBy: User?,
)