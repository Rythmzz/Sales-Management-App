package com.tm968.shoppev4.page.presentation.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.data.model.Order
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class OrderHistoryViewModel(private val repository: Repository):ViewModel() {
    private val _success = MutableSharedFlow<List<Order>>()
    val success:SharedFlow<List<Order>> get() = _success

    fun getListOrder(){
        viewModelScope.launch {
            when(val Result=repository.getListOrder()){
                is BaseResponse.Success -> {
                    _success.emit(Result.data)
                }
                is BaseResponse.Error -> {

                }
                else -> {}

            }
        }
    }
}