package com.example.grocerylist.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.grocerylist.database.UserDB.Companion.toUser
import com.example.grocerylist.domain.GroceryListProduct
import com.example.grocerylist.domain.User
import java.util.*

@Entity(tableName = "grocery_list_products")
data class GroceryListProductDB(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "product_id")
    val id: UUID,
    @ColumnInfo(name = "grocery_list_id")
    val groceryListId: UUID,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: Int?,
    @ColumnInfo(name = "unit")
    val unit: String?,
    @ColumnInfo(name = "added_by_user_id")
    val addedByUserId: UUID,
    @ColumnInfo(name = "added_by_user_name")
    val addedByUserName: String,
    @ColumnInfo(name = "remark")
    val remark: String?,
    @ColumnInfo(name = "updated_by_user_id")
    val updatedByUserId: UUID?,
    @ColumnInfo(name = "updated_by_user_name")
    val updatedByUserName: String?,
)

fun GroceryListProductDB.asDomainModel(): GroceryListProduct {
    return GroceryListProduct(
        id = id,
        groceryListId = groceryListId,
        name = name,
        amount = amount,
        unit = unit,
        addedBy = User(addedByUserId, addedByUserName),
        remark = remark,
        updatedBy = toUser(updatedByUserId, updatedByUserName),
    )
}