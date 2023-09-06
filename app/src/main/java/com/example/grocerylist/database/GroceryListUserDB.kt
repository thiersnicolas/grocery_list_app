package com.example.grocerylist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.grocerylist.domain.GroceryListUser
import com.example.grocerylist.domain.User
import java.util.*

@Entity(tableName = "grocery_list_users", primaryKeys = ["user_id", "grocery_list_id"])
data class GroceryListUserDB(
    @ColumnInfo(name = "user_id")
    val id: UUID,
    @ColumnInfo(name = "grocery_list_id")
    val groceryListId: UUID,
    @ColumnInfo(name = "name")
    val name: String,
)

fun GroceryListUserDB.asDomainModel(): GroceryListUser {
    return GroceryListUser(
        User(id, name),
        groceryListId = groceryListId,
    )
}