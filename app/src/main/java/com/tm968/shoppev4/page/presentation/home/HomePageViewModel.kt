package com.tm968.shoppev4.page.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class HomePageViewModel(private val repository: Repository) : ViewModel() {
    private val _loading = MutableSharedFlow<Boolean>()
    val loading :SharedFlow<Boolean> get() = _loading

    private val _ProductList = MutableSharedFlow<List<Product>>()
    val ProductList : SharedFlow<List<Product>> get() = _ProductList

    private val _ProductListbyView = MutableSharedFlow<List<Product>>()
    val ProductListbyView :SharedFlow<List<Product>> get() = _ProductListbyView

    fun getProductList(){
        viewModelScope.launch {
            when(val Result = repository.getProductList()){
                is BaseResponse.Success -> {
                    _ProductList.emit(Result.data)

                }
                is BaseResponse.Error -> {
                }

                else -> {}
            }
        }
    }

    fun getProductListShowView(){
        viewModelScope.launch {
            when(val Result = repository.getProductListShowView()){
                is BaseResponse.Success -> {
                    _ProductListbyView.emit(Result.data)

                }
                is BaseResponse.Error -> {

                }

                else -> {}
            }
        }

    }

}