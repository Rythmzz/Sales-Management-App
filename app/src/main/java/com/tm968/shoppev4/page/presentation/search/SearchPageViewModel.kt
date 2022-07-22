package com.tm968.shoppev4.page.presentation.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class SearchPageViewModel(private val repository: Repository) : ViewModel() {

    private val _searchtype = MutableSharedFlow<List<Product>>()
    val searchtype:SharedFlow<List<Product>> get() = _searchtype

    fun SearchProduct(key:String){
        viewModelScope.launch {
            when(val Result = repository.getProductSearch(key)){
                is BaseResponse.Success -> {
                    _searchtype.emit(Result.data)
                }
                is BaseResponse.Error -> {
                    Log.d("ERROR-DEBUG",Result.exception.toString())
                }
                else -> {}
            }

        }

    }

}