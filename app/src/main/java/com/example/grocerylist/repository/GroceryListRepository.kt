package com.example.grocerylist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.grocerylist.TokenSessionManager
import com.example.grocerylist.database.*
import com.example.grocerylist.domain.GroceryList
import com.example.grocerylist.domain.GroceryListDetail
import com.example.grocerylist.domain.GroceryListProduct
import com.example.grocerylist.domain.GroceryListUser
import com.example.grocerylist.network.GroceryListApiService
import com.example.grocerylist.network.resources.CreateGroceryListRequest
import com.example.grocerylist.network.resources.GroceryListUsersResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceryListRepository @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val groceryListApiService: GroceryListApiService,
    private val groceryListDao: GroceryListDao,
    private val groceryListDetailDao: GroceryListDetailDao,
    private val groceryListProductDao: GroceryListProductDao,
    private val groceryListUserDao: GroceryListUserDao,
) {
    val groceryLists: LiveData<List<GroceryList>> =
        Transformations.map(groceryListDao.getGroceryLists()) { groceryListDBList ->
            groceryListDBList.map { groceryListDB -> groceryListDB.asDomainModel() }
        }

    suspend fun getGroceryListDetail(groceryListId: UUID): LiveData<GroceryListDetail> {
        val groceryListDetail =
            Transformations.map(groceryListDetailDao.getGroceryListDetail(groceryListId)) { groceryListDetailDB ->
                groceryListDetailDB.asDomainModel()
            }
        refreshGroceryListDetail(groceryListId)
        return groceryListDetail;
    }

    suspend fun deleteGroceryListDetail(groceryListId: UUID) {
        withContext(Dispatchers.IO) {
            groceryListApiService.deleteGroceryListDetail(groceryListId.toString())
            clearData()
            refreshGroceryLists()
        }
    }

    suspend fun refreshGroceryLists() {
        withContext(Dispatchers.IO) {
            Timber.tag("GroceryListRepository")
                .i("refreshGroceryLists for user: %s", getCurrentUserId())
            getCurrentUserId()?.let { userId ->
                val groceryListFromApi =
                    groceryListApiService.getGroceryListsForUser(userId).await()
                groceryListDao.insertGroceryLists(groceryListFromApi.mapToGroceryLists().map {
                    GroceryListDB(
                        it.id,
                        it.name,
                        it.createdBy,
                        it.createdDate,
                        it.status
                    )
                })
            }
        }
    }

    suspend fun createGroceryList(name: String): LiveData<GroceryListDetail> {
        var groceryListDetail: LiveData<GroceryListDetail> = MutableLiveData(null)
        withContext(Dispatchers.IO) {
            val groceryListDetailFromApi =
                groceryListApiService.createGroceryList(CreateGroceryListRequest(name)).await()
            groceryListDetailDao.insertGroceryListDetail(
                groceryListDetailFromApi.mapToGroceryListDetail().let {
                    GroceryListDetailDB(
                        it.id,
                        it.name,
                        it.createdBy.id,
                        it.createdBy.name,
                        it.createdDate,
                        it.status,
                        it.collectBy?.id,
                        it.collectBy?.name,
                        it.currency,
                        it.price
                    )
                })
            groceryListDetail = Transformations.map(
                groceryListDetailDao.getGroceryListDetail(
                    UUID.fromString(groceryListDetailFromApi.id)
                )
            ) {
                it.asDomainModel()
            }
            refreshGroceryLists()
        }
        return groceryListDetail
    }


    suspend fun refreshGroceryListDetail(groceryListId: UUID) {
        withContext(Dispatchers.IO) {
            val groceryListDetailFromApi =
                groceryListApiService.getGroceryListDetail(groceryListId.toString()).await()
            groceryListDetailDao.insertGroceryListDetail(
                groceryListDetailFromApi.mapToGroceryListDetail().let {
                    GroceryListDetailDB(
                        it.id,
                        it.name,
                        it.createdBy.id,
                        it.createdBy.name,
                        it.createdDate,
                        it.status,
                        it.collectBy?.id,
                        it.collectBy?.name,
                        it.currency,
                        it.price
                    )
                })
        }
    }

    suspend fun clearData() {
        groceryListDao.deleteAllGroceryLists()
        groceryListDetailDao.deleteAllGroceryListDetails()
        groceryListProductDao.deleteAllGroceryListProducts()
        groceryListUserDao.deleteAllGroceryListUsers()
    }

    fun getCurrentUserId(): String? {
        return tokenSessionManager.fetchUserId()?.toString()
    }

    companion object {
        private val gson: Gson = Gson()
    }
}
