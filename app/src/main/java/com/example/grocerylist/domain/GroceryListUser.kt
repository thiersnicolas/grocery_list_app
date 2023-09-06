package com.example.grocerylist.domain

import java.util.*

data class GroceryListUser(
    val user: User,
    val groceryListId: UUID,
)