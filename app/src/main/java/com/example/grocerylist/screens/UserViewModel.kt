package com.example.grocerylist.screens

import android.widget.ScrollView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.domain.AppUser
import com.example.grocerylist.model.network.rest.resources.LoginRequest
import com.example.grocerylist.model.network.rest.resources.LoginResponse
import com.example.grocerylist.model.network.rest.resources.RegisterRequest
import com.example.grocerylist.repository.AppUserRepository
import com.example.grocerylist.repository.GroceryListRepository
import com.example.grocerylist.screens.grocerylists.GroceryListsApiStatus
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

enum class UserApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class UserViewModel @Inject constructor(
    private val appUserRepository: AppUserRepository,
    private val groceryListRepository: GroceryListRepository
) :
    ViewModel() {

    val activeUser: LiveData<AppUser?>
        get() = appUserRepository.activeUser

    private val _status = MutableLiveData<UserApiStatus>()
    val status: LiveData<UserApiStatus>
        get() = _status

    fun login(loginRequest: LoginRequest, loginScrollView: ScrollView) {

        _status.value = UserApiStatus.LOADING
        appUserRepository.attemptLogin(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.mapToUser()
                    viewModelScope.launch {
                        appUserRepository.loginSuccessful(user, response.body()!!.token)
                    }
                    _status.value = UserApiStatus.DONE
                } else {
                    _status.value = UserApiStatus.ERROR
                    Snackbar.make(
                        loginScrollView,
                        "Login failed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Snackbar.make(
                    loginScrollView,
                    "Login failed, unable to connect",
                    Snackbar.LENGTH_LONG
                ).show()
                Timber.tag("logging").d("Could not login user")
                _status.value = UserApiStatus.ERROR
            }
        })
    }

    fun register(registerRequest: RegisterRequest, loginScrollView: ScrollView) {
        _status.value = UserApiStatus.LOADING
        appUserRepository.register(registerRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()!!.mapToUser()
                    viewModelScope.launch {
                        appUserRepository.loginSuccessful(user, response.body()!!.token)
                    }
                    _status.value = UserApiStatus.DONE
                } else {
                    _status.value = UserApiStatus.ERROR
                    Snackbar.make(
                        loginScrollView,
                        "Register failed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Snackbar.make(
                    loginScrollView,
                    "Register failed, unable to connect",
                    Snackbar.LENGTH_LONG
                ).show()
                Timber.tag("logging").d("Could not register user")
                _status.value = UserApiStatus.ERROR
            }
        })
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            groceryListRepository.clearData()
            appUserRepository.deleteCurrentUser()
        }
    }

    override fun onCleared() {
        Timber.tag("USER_VIEW_MODEL").d("Clearing the viewmodel...")
        super.onCleared()
    }
}
