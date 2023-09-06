package com.example.grocerylist.network

import com.example.grocerylist.network.resources.*
import com.example.grocerylist.network.rest.resources.GroceryListsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface GroceryListApiService {

    @Headers("Content-Type:application/json")
    @GET("/api/grocery-lists")
    fun getGroceryListsForUser(@Query("userId") userId: String): Call<GroceryListsResponse>

    @Headers("Content-Type:application/json")
    @GET("/api/grocery-lists/{groceryListId}")
    fun getGroceryListDetail(@Path("groceryListId") groceryListId: String): Call<GroceryListDetailResponse>

    @Headers("Content-Type:application/json")
    @DELETE("/api/grocery-lists/{groceryListId}")
    suspend fun deleteGroceryListDetail(@Path("groceryListId") groceryListId: String): Response<ResponseBody>

    @Headers("Content-Type:application/json")
    @GET("/api/grocery-lists/{groceryListId}/users")
    fun getGroceryListUsers(@Path("groceryListId") groceryListId: String): Call<GroceryListUsersResponse>

    @Headers("Content-Type:application/json")
    @GET("/api/grocery-lists/{groceryListId}/products")
    fun getGroceryListProducts(@Path("groceryListId") groceryListId: String): Call<GroceryListProductsResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/grocery-lists")
    fun createGroceryList(@Body createGroceryListRequest: CreateGroceryListRequest): Call<GroceryListDetailResponse>

}