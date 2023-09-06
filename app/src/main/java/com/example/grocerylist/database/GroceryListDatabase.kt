package com.example.grocerylist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [AppUserDB::class, GroceryListDB::class, GroceryListDetailDB::class, GroceryListProductDB::class, GroceryListUserDB::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(GroceryListTypeConverters::class)
abstract class GroceryListDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val groceryListDao: GroceryListDao
    abstract val groceryListDetailDao: GroceryListDetailDao
    abstract val groceryListProductDao: GroceryListProductDao
    abstract val groceryListUserDao: GroceryListUserDao


    companion object {

        @Volatile
        private var INSTANCE: GroceryListDatabase? = null

        fun getInstance(context: Context): GroceryListDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        GroceryListDatabase::class.java,
                        "grocery_list_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}