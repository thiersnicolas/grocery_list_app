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
import java.util.UUID
import javax.inject.Inject

enum class GroceryListsApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class GroceryListsViewModel @Inject constructor(private val groceryListRepository: GroceryListRepository) :
    ViewModel() {

    val groceryLists: LiveData<List<GroceryList>>
        get() = groceryListRepository.groceryLists

    private val _status = MutableLiveData<GroceryListsApiStatus>()
    val status: LiveData<GroceryListsApiStatus>
        get() = _status

    private val _navigateToGroceryListDetail = MutableLiveData<UUID?>()
    val navigateToGroceryListDetail
        get() = _navigateToGroceryListDetail

    private val _navigateToCreateGroceryList = MutableLiveData(false)
    val navigateToCreateGroceryList
        get() = _navigateToCreateGroceryList

    fun refreshGroceryLists() {
        Timber.tag("GroceryListsViewModel").i("refreshGroceryLists")
        viewModelScope.launch {
            _status.value = GroceryListsApiStatus.LOADING
            groceryListRepository.refreshGroceryLists()
            _status.value = GroceryListsApiStatus.DONE
        }
    }

    fun onCreateClicked() {
        _navigateToCreateGroceryList.value = true
    }

    fun doneNavigatingToCreateGroceryList() {
        _navigateToCreateGroceryList.value = false
    }

    fun onGroceryListClicked(id: UUID) {
        _navigateToGroceryListDetail.value = id
    }

    fun doneNavigatingToGroceryListDetails() {
        _navigateToGroceryListDetail.value = null
    }

    override fun onCleared() {
        Timber.tag("USER_VIEW_MODEL").d("Clearing the viewmodel...")
        super.onCleared()
    }
}
