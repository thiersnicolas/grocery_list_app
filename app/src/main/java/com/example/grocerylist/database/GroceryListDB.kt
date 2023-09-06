package com.example.grocerylist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerylist.domain.GroceryList
import com.example.grocerylist.domain.User
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "grocery_lists")
data class GroceryListDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "grocery_list_id")
    val id: UUID,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "created_by")
    val createdBy: UUID,
    @ColumnInfo(name = "created_date")
    val createdDate: LocalDateTime,
    @ColumnInfo(name = "status")
    val status: String
)

fun GroceryListDB.asDomainModel(): GroceryList {
    return GroceryList(
        id = id,
        name = name,
        createdBy = createdBy,
        createdDate = createdDate,
        status = status
    )
}
