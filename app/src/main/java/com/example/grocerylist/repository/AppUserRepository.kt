package com.example.grocerylist.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.grocerylist.TokenSessionManager
import com.example.grocerylist.database.AppUserDB
import com.example.grocerylist.database.UserDao
import com.example.grocerylist.database.asDomainModel
import com.example.grocerylist.domain.AppUser
import com.example.grocerylist.network.UserApiService
import com.example.grocerylist.model.network.rest.resources.LoginRequest
import com.example.grocerylist.model.network.rest.resources.LoginResponse
import com.example.grocerylist.model.network.rest.resources.RegisterRequest
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppUserRepository @Inject constructor(
    private val tokenSessionManager: TokenSessionManager,
    private val userApiService: UserApiService,
    private val userDao: UserDao
) {
    val activeUser: LiveData<AppUser?>
        get() = Transformations.map(userDao.getAppUser()) { it?.asDomainModel() }

    fun attemptLogin(loginRequest: LoginRequest): Call<LoginResponse> {
        return userApiService.login(loginRequest)
    }

    fun register(registerRequest: RegisterRequest): Call<LoginResponse> {
        return userApiService.register(registerRequest)
    }

    suspend fun loginSuccessful(user: AppUser, userToken: String) {
        userDao.insertAppUser(AppUserDB(user.id, user.name, user.email))
        tokenSessionManager.saveUserId(user.id.toString())
        tokenSessionManager.saveAuthToken(userToken)
    }

    suspend fun deleteCurrentUser() {
        userDao.deleteAppUser()
        tokenSessionManager.deleteAuthToken()
        tokenSessionManager.deleteAuthId()
    }
}
