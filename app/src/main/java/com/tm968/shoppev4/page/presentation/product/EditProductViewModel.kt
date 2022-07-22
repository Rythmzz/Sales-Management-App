package com.tm968.shoppev4.page.presentation.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.base.ResponseError
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class EditProductViewModel(private val repository: Repository):ViewModel() {
    private val _loading = MutableSharedFlow<Boolean>()
    val loading:SharedFlow<Boolean> get() = _loading

    private val _success = MutableSharedFlow<Any>()
    val success:SharedFlow<Any> get() = _success

    fun editProduct(id:Int,name:String,imageUrl:String,price:Double,salePrice:Double,categoryId:Int,view:Int,description:String){
        viewModelScope.launch {
            _loading.emit(true)
            when (val Result=repository.editProduct(id,name,imageUrl,price,salePrice,categoryId,view,description)){
                is BaseResponse.Success -> {
                    _success.emit(true)
                    _loading.emit(false)
                }
                is BaseResponse.Error -> {
                    _success.emit((Result.exception as ResponseError).msg)
                    _loading.emit(false)
                }
                BaseResponse.Loading -> Unit
            }
        }

    }
}