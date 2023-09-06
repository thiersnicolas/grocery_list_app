package com.example.grocerylist

import android.app.Application
import com.example.grocerylist.database.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun getGroceryListDatabase(context: Application): GroceryListDatabase {
        return GroceryListDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun getUserDao(groceryListDatabase: GroceryListDatabase): UserDao {
        return groceryListDatabase.userDao
    }

    @Provides
    @Singleton
    fun getGroceryListDao(groceryListDatabase: GroceryListDatabase): GroceryListDao {
        return groceryListDatabase.groceryListDao
    }

    @Provides
    @Singleton
    fun getGroceryListDetailDao(groceryListDatabase: GroceryListDatabase): GroceryListDetailDao {
        return groceryListDatabase.groceryListDetailDao
    }

    @Provides
    @Singleton
    fun getGroceryListProductDao(groceryListDatabase: GroceryListDatabase): GroceryListProductDao {
        return groceryListDatabase.groceryListProductDao
    }

    @Provides
    @Singleton
    fun getGroceryListUserDao(groceryListDatabase: GroceryListDatabase): GroceryListUserDao {
        return groceryListDatabase.groceryListUserDao
    }
}
