package com.tm968.shoppev4.page.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.data.model.Product
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class AllProductPageViewModel(private val repository: Repository):ViewModel() {
    private val _productList=MutableSharedFlow<List<Product>>()
    val productList:SharedFlow<List<Product>> get() = _productList

    fun getProductList(){
        viewModelScope.launch {
            when(val Result=repository.getProductList()){
                is BaseResponse.Success -> {
                    _productList.emit(Result.data)
                }
                is BaseResponse.Error ->{

                }
                else -> {}
            }
        }
    }
}