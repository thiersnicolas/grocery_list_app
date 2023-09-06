package com.example.grocerylist.screens.grocerylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.domain.GroceryListDetail
import com.example.grocerylist.repository.GroceryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class CreateGroceryListApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class CreateGroceryListViewModel @Inject constructor(private val groceryListRepository: GroceryListRepository) :
    ViewModel() {

    var _groceryListDetail: LiveData<GroceryListDetail?> = MutableLiveData(null)
    val groceryListDetail: LiveData<GroceryListDetail?>
        get() = _groceryListDetail

    private val _status = MutableLiveData<CreateGroceryListApiStatus>()
    val status: LiveData<CreateGroceryListApiStatus>
        get() = _status

    fun createGroceryList(name: String) {
        viewModelScope.launch {
            _status.value = CreateGroceryListApiStatus.LOADING
            _groceryListDetail = groceryListRepository.createGroceryList(name)
            _status.value = CreateGroceryListApiStatus.DONE
        }
    }

    fun clearGroceryListDetail() {
        _groceryListDetail = MutableLiveData(null)
    }

    override fun onCleared() {
        Timber.tag("USER_VIEW_MODEL").d("Clearing the viewmodel...")
        super.onCleared()
    }


}
