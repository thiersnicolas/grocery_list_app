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
import java.util.*
import javax.inject.Inject

enum class GroceryListDetailApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class GroceryListDetailViewModel @Inject constructor(
    private val groceryListRepository: GroceryListRepository
) :
    ViewModel() {

    var groceryListDetail: LiveData<GroceryListDetail> = MutableLiveData(null)

    private val _status = MutableLiveData<GroceryListDetailApiStatus>()
    val status: LiveData<GroceryListDetailApiStatus>
        get() = _status

    private val _navigateToGroceryLists = MutableLiveData<UUID?>()
    val navigateToGroceryLists: LiveData<UUID?>
        get() = _navigateToGroceryLists

    fun getGroceryListDetail(groceryListId: UUID) {
        Timber.tag("GroceryListDetailViewModel").i("getGroceryListDetail")
        viewModelScope.launch {
            _status.value = GroceryListDetailApiStatus.LOADING
            groceryListDetail = groceryListRepository.getGroceryListDetail(groceryListId)
            _status.value = GroceryListDetailApiStatus.DONE
        }
    }

    fun refreshGroceryListDetail() {
        Timber.tag("GroceryListDetailViewModel").i("refreshGroceryListDetail")
        viewModelScope.launch {
            _status.value = GroceryListDetailApiStatus.LOADING
            groceryListDetail.value?.let { groceryListRepository.refreshGroceryListDetail(it.id) }
            _status.value = GroceryListDetailApiStatus.DONE
        }
    }

    fun onDeleteClicked(id: UUID?) {
        _navigateToGroceryLists.value = id
    }

    fun deleteGroceryListDetail(id: UUID) {
        viewModelScope.launch {
            _status.value = GroceryListDetailApiStatus.LOADING
            groceryListRepository.deleteGroceryListDetail(id)
            _status.value = GroceryListDetailApiStatus.DONE
        }
    }

    fun doneNavigatingToGroceryLists() {
        _navigateToGroceryLists.value = null
    }

}
