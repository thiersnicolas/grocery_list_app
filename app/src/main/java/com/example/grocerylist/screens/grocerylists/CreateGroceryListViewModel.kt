package com.example.grocerylist.screens.grocerylists

import android.provider.Settings.Global.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.grocerylist.R
import com.example.grocerylist.repository.GroceryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class CreateGroceryListApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class CreateGroceryListViewModel @Inject constructor(private val groceryListRepository: GroceryListRepository) :
    ViewModel() {
    private val _status = MutableLiveData<CreateGroceryListApiStatus>()
    val status: LiveData<CreateGroceryListApiStatus>
        get() = _status

    private val _navigateToGroceryListDetail = MutableLiveData<UUID?>()
    val navigateToGroceryListDetail
        get() = _navigateToGroceryListDetail

    private val _displayError = MutableLiveData<Boolean?>(null)
    val displayError
        get() = _displayError

    fun createGroceryList(name: String) {
        val trimmedName = name.trim()
        if (isNameValid(trimmedName)) {
                viewModelScope.launch {
                    _status.value = CreateGroceryListApiStatus.LOADING
                    groceryListRepository.createGroceryList(trimmedName)
                        .observeForever { groceryListDetail ->
                            groceryListDetail?.let { _navigateToGroceryListDetail.value = it.id }

                        }
                    _status.value = CreateGroceryListApiStatus.DONE
                }
            } else {
            _displayError.value = false
        }
    }

    fun doneNavigatingToGroceryListDetails() {
        _navigateToGroceryListDetail.value = null
    }

    private fun isNameValid(name: String): Boolean {
        return !(name.isBlank() || name.length <= 3)
    }

}
