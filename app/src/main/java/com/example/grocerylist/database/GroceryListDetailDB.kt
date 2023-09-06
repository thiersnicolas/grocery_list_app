package com.example.grocerylist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerylist.database.UserDB.Companion.toUser
import com.example.grocerylist.domain.GroceryListDetail
import com.example.grocerylist.domain.User
import java.time.LocalDateTime
import java.util.*

@Entity(tableName = "grocery_list_details")
data class GroceryListDetailDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "grocery_list_id")
    val id: UUID,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "created_by_user_id")
    val createdByUserId: UUID,
    @ColumnInfo(name = "created_by_user_name")
    val createdByUserName: String,
    @ColumnInfo(name = "created_date")
    val createdDate: LocalDateTime,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "collect_by_user_id")
    val collectByUserId: UUID?,
    @ColumnInfo(name = "collect_by_user_name")
    val collectByUserName: String?,
    @ColumnInfo(name = "currency")
    val currency: String?,
    @ColumnInfo(name = "price")
    val price: Int?
)

fun GroceryListDetailDB.asDomainModel(): GroceryListDetail {
    return GroceryListDetail(
        id = id,
        name = name,
        createdBy = User(createdByUserId, createdByUserName),
        createdDate = createdDate,
        status = status,
        collectBy = toUser(collectByUserId, collectByUserName),
        currency = currency,
        price = price
    )
}
