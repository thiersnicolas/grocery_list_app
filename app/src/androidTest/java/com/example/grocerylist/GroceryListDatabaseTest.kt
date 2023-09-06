package com.example.grocerylist

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.grocerylist.database.GroceryListDB
import com.example.grocerylist.database.GroceryListDao
import com.example.grocerylist.database.GroceryListDatabase
import com.example.grocerylist.database.asDomainModel
import com.example.grocerylist.domain.GroceryList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime
import java.util.*

@RunWith(AndroidJUnit4::class)
class GroceryListDatabaseTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var groceryListDao: GroceryListDao
    private lateinit var db: GroceryListDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GroceryListDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        groceryListDao = db.groceryListDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGroceryList() {
        val groceryList =
            GroceryList(
                UUID.randomUUID(),
                "name",
                UUID.randomUUID(),
                LocalDateTime.now(),
                "status"
            )
        val groceryListDB = GroceryListDB(
            groceryList.id,
            groceryList.name,
            groceryList.createdBy,
            groceryList.createdDate,
            groceryList.status
        )
        runBlocking {
            groceryListDao.insertGroceryLists(listOf(groceryListDB))
        }
        groceryListDao.getGroceryLists().observeForever {
            assertEquals(it[0], groceryListDB)
            assertEquals(it[0].asDomainModel(), groceryList)
        }
    }
}