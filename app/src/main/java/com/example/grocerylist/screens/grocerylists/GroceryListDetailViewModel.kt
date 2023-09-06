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
import java.util.UUID
import javax.inject.Inject

enum class GroceryListDetailApiStatus { LOADING, ERROR, DONE }

@HiltViewModel
class GroceryListDetailViewModel @Inject constructor(private val groceryListRepository: GroceryListRepository) :
    ViewModel() {

    val groceryListDetail: LiveData<GroceryListDetail?>
        get() = groceryListRepository._groceryListDetail

    private val _status = MutableLiveData<GroceryListDetailApiStatus>()
    val status: LiveData<GroceryListDetailApiStatus>
        get() = _status

    private val _deleteClicked = MutableLiveData(false)
    val deleteClicked: LiveData<Boolean>
        get() = _deleteClicked

    fun getGroceryListDetail(groceryListId: UUID) {
        viewModelScope.launch {
            _status.value = GroceryListDetailApiStatus.LOADING
            groceryListRepository.getGroceryListDetail(groceryListId)
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

    fun deleteGroceryListDetail() {
        _deleteClicked.value = true
        viewModelScope.launch {
            groceryListDetail.value?.let {
                groceryListRepository.deleteGroceryListDetail(it.id)
            }
        }
    }

    override fun onCleared() {
        Timber.tag("USER_VIEW_MODEL").d("Clearing the viewmodel...")
        super.onCleared()
    }
}
