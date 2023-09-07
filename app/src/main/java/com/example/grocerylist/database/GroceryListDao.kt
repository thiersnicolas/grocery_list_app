package com.example.grocerylist.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GroceryListDao {

    @Query("SELECT * FROM grocery_lists")
    fun getGroceryLists(): LiveData<List<GroceryListDB>>

    @Query("SELECT * FROM grocery_lists where name LIKE '%' || :search || '%'")
    fun getGroceryLists(search: String): LiveData<List<GroceryListDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroceryLists(groceryLists: List<GroceryListDB>)

    @Query("DELETE FROM grocery_lists")
    fun deleteAllGroceryLists()
}
