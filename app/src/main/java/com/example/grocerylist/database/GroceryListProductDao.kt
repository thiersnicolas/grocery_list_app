package com.example.grocerylist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface GroceryListProductDao {

    @Query("SELECT * FROM grocery_list_products WHERE grocery_list_id = :groceryListId")
    fun getGroceryListProducts(groceryListId: UUID): LiveData<List<GroceryListProductDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryListProducts(groceryLists: List<GroceryListProductDB>)

    @Query("DELETE FROM grocery_list_products")
    suspend fun deleteAllGroceryListProducts()
}
