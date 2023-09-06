package com.example.grocerylist.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface GroceryListUserDao {

    @Query("SELECT * FROM grocery_list_users WHERE grocery_list_id = :groceryListId")
    fun getGroceryListUsers(groceryListId: UUID): LiveData<List<GroceryListUserDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroceryListUsers(groceryListUsers: List<GroceryListUserDB>)

    @Query("DELETE FROM grocery_list_users")
    suspend fun deleteAllGroceryListUsers()
}
