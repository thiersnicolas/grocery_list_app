package com.example.grocerylist.screens.grocerylists

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerylist.domain.GroceryList
import com.example.grocerylist.repository.GroceryListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

enum class GroceryListsApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class GroceryListsViewModel @Inject constructor(private val groceryListRepository: GroceryListRepository) :
    ViewModel() {

    val groceryLists: LiveData<List<GroceryList>>
        get() = groceryListRepository._groceryLists

    private val _status = MutableLiveData<GroceryListsApiStatus>()
    val status: LiveData<GroceryListsApiStatus>
        get() = _status

    private val _createGroceryListClicked = MutableLiveData(false)
    val createGroceryListClicked
        get() = _createGroceryListClicked

    init {
        Timber.i("getting groceryLists")
        viewModelScope.launch {
            _status.value = GroceryListsApiStatus.LOADING
            groceryListRepository.getGroceryLists()
            _status.value = GroceryListsApiStatus.DONE
        }
    }

    fun refreshGroceryLists() {
        Timber.tag("GroceryListsViewModel").i("refreshGroceryLists")
        viewModelScope.launch {
            _status.value = GroceryListsApiStatus.LOADING
            groceryListRepository.refreshGroceryLists()
            _status.value = GroceryListsApiStatus.DONE
        }
    }

    fun createClicked() {
        _createGroceryListClicked.value = true
    }

    override fun onCleared() {
        Timber.tag("USER_VIEW_MODEL").d("Clearing the viewmodel...")
        super.onCleared()
    }
}
