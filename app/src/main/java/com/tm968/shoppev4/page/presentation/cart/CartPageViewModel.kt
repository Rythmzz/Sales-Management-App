package com.tm968.shoppev4.page.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.data.model.OrderRequest
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class CartPageViewModel(private val repository: Repository):ViewModel() {
    private val _success = MutableSharedFlow<Boolean>()
    val success:SharedFlow<Boolean> get() = _success

    private val _loading = MutableSharedFlow<Boolean>()
    val loading :SharedFlow<Boolean> get() = _loading

    fun createOrder(orderRequest: OrderRequest){
        viewModelScope.launch {
            _loading.emit(true)
            when(val Result = repository.createOrder(orderRequest)){
                is BaseResponse.Success -> {
                    _success.emit(true)
                    _loading.emit(false)
                }
                is BaseResponse.Error ->{
                    _success.emit(false)
                    _loading.emit(false)
                }
                BaseResponse.Loading -> Unit
            }
        }

    }


}