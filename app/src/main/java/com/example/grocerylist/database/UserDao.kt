package com.example.grocerylist.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM user LIMIT 1")
    fun getAppUser(): LiveData<AppUserDB?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppUser(user: AppUserDB)

    @Query("DELETE FROM user")
    suspend fun deleteAppUser()
}
