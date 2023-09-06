package com.example.grocerylist.network

import com.example.grocerylist.model.network.rest.resources.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserApiService {

    @Headers("Content-Type:application/json")
    @POST("/api/users/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @POST("/api/users/register")
    fun register(@Body registerRequest: RegisterRequest): Call<LoginResponse>
}
