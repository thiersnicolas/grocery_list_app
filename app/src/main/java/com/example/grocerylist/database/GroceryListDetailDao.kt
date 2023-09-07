package com.example.grocerylist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface GroceryListDetailDao {

    @Query("SELECT * FROM grocery_list_details WHERE grocery_list_id = :groceryListId")
    fun getGroceryListDetail(groceryListId: UUID): LiveData<GroceryListDetailDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroceryListDetail(groceryListDetail: GroceryListDetailDB)

    @Query("DELETE FROM grocery_list_details")
    suspend fun deleteAllGroceryListDetails()
}
