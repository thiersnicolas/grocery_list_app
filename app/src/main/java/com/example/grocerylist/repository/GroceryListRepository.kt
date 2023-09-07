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
import com.example.grocerylist.network.resources.GroceryListDetailResponse
import com.example.grocerylist.network.resources.GroceryListProductsResponse
import com.example.grocerylist.network.resources.GroceryListUsersResponse
import com.example.grocerylist.network.rest.resources.GroceryListsResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GroceryListRepository @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val appUserRepository: AppUserRepository,
    private val groceryListApiService: GroceryListApiService,
    private val groceryListDao: GroceryListDao,
    private val groceryListDetailDao: GroceryListDetailDao,
    private val groceryListProductDao: GroceryListProductDao,
    private val groceryListUserDao: GroceryListUserDao,
) {
    private val _groceryListProducts = MutableLiveData<List<GroceryListProduct>>(emptyList())
    private val _groceryListUsers = MutableLiveData<List<GroceryListUser>>(emptyList())

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
                groceryListApiService.getGroceryListsForUser(userId)
                    .enqueue(object : Callback<GroceryListsResponse?> {
                        override fun onResponse(
                            call: Call<GroceryListsResponse?>,
                            response: Response<GroceryListsResponse?>
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                val groceryListsFromApi = response.body()!!.mapToGroceryLists()
                                launch {
                                    groceryListDao.insertGroceryLists(groceryListsFromApi.map {
                                        GroceryListDB(
                                            it.id,
                                            it.name,
                                            it.createdBy,
                                            it.createdDate,
                                            it.status
                                        )
                                    })
                                    Timber.tag("GroceryListRepository")
                                        .i("groceryListDao.insertGroceryLists done")
                                }
                                Timber.tag("GroceryListRepository").i(
                                    "refreshGroceryLists done: %s",
                                    gson.toJson(groceryListsFromApi.toString())
                                )
                            } else if (response.code() == 401) {
                                launch {
                                    appUserRepository.deleteCurrentUser()
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<GroceryListsResponse?>,
                            error: Throwable
                        ) {
                            Timber.i(error)
                        }
                    })
            }
        }
    }

    suspend fun createGroceryList(name: String): LiveData<GroceryListDetail> {
        var groceryListDetail: LiveData<GroceryListDetail> = MutableLiveData(null)
        withContext(Dispatchers.IO) {
            groceryListApiService.createGroceryList(CreateGroceryListRequest(name))
                .enqueue(object : Callback<GroceryListDetailResponse?> {
                    override fun onResponse(
                        call: Call<GroceryListDetailResponse?>,
                        response: Response<GroceryListDetailResponse?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val groceryListDetailFromApi =
                                response.body()!!.mapToGroceryListDetail()
                            launch {
                                groceryListDetailDao.insertGroceryListDetail(
                                    groceryListDetailFromApi.let {
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
                                groceryListDetail =
                                    Transformations.map(
                                        groceryListDetailDao.getGroceryListDetail(
                                            groceryListDetailFromApi.id
                                        )
                                    ) {
                                        it.asDomainModel()
                                    }
                                refreshGroceryLists()
                            }
                        } else if (response.code() == 401) {
                            launch {
                                appUserRepository.deleteCurrentUser()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<GroceryListDetailResponse?>,
                        error: Throwable
                    ) {
                        Timber.i(error)
                    }
                })
        }
        return groceryListDetail
    }

    suspend fun refreshGroceryListDetail(groceryListId: UUID) {
        withContext(Dispatchers.IO) {
            groceryListApiService.getGroceryListDetail(groceryListId.toString())
                .enqueue(object : Callback<GroceryListDetailResponse?> {
                    override fun onResponse(
                        call: Call<GroceryListDetailResponse?>,
                        response: Response<GroceryListDetailResponse?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val groceryListDetailFromApi =
                                response.body()!!.mapToGroceryListDetail()
                            launch {
                                groceryListDetailDao.insertGroceryListDetail(
                                    groceryListDetailFromApi.let {
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
                        } else if (response.code() == 401) {
                            launch {
                                appUserRepository.deleteCurrentUser()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<GroceryListDetailResponse?>,
                        error: Throwable
                    ) {
                        Timber.i(error)
                    }
                })
        }
    }

    suspend fun getGroceryListProducts(groceryListId: UUID): LiveData<List<GroceryListProduct>> {
        withContext(Dispatchers.IO) {
            groceryListApiService.getGroceryListProducts(groceryListId.toString())
                .enqueue(object : Callback<GroceryListProductsResponse?> {
                    override fun onResponse(
                        call: Call<GroceryListProductsResponse?>,
                        response: Response<GroceryListProductsResponse?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val groceryListProductsFromApi =
                                response.body()!!.mapToGroceryListProducts(groceryListId)
                            launch {
                                groceryListProductDao.insertGroceryListProducts(
                                    groceryListProductsFromApi.map {
                                        GroceryListProductDB(
                                            it.id,
                                            it.groceryListId,
                                            it.name,
                                            it.amount,
                                            it.unit,
                                            it.addedBy.id,
                                            it.addedBy.name,
                                            it.remark,
                                            it.updatedBy?.id,
                                            it.updatedBy?.name,
                                        )
                                    })
                            }

                            _groceryListProducts.value = groceryListProductsFromApi
                        } else if (response.code() == 401) {
                            launch {
                                appUserRepository.deleteCurrentUser()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<GroceryListProductsResponse?>,
                        error: Throwable
                    ) {
                        Timber.i(error)
                    }
                })
        }
        return _groceryListProducts
    }

    suspend fun getGroceryListUsers(groceryListId: UUID): LiveData<List<GroceryListUser>> {
        withContext(Dispatchers.IO) {
            groceryListApiService.getGroceryListUsers(groceryListId.toString())
                .enqueue(object : Callback<GroceryListUsersResponse?> {
                    override fun onResponse(
                        call: Call<GroceryListUsersResponse?>,
                        response: Response<GroceryListUsersResponse?>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val groceryListUsersFromApi =
                                response.body()!!.mapToGroceryListUsers(groceryListId)
                            launch {
                                groceryListUserDao.insertGroceryListUsers(
                                    groceryListUsersFromApi.map {
                                        GroceryListUserDB(
                                            it.user.id,
                                            it.groceryListId,
                                            it.user.name
                                        )
                                    })
                            }

                            _groceryListUsers.value = groceryListUsersFromApi
                        } else if (response.code() == 401) {
                            launch {
                                appUserRepository.deleteCurrentUser()
                            }
                        }
                    }

                    override fun onFailure(
                        call: Call<GroceryListUsersResponse?>,
                        error: Throwable
                    ) {
                        Timber.i(error)
                    }
                })
        }
        return _groceryListUsers
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
