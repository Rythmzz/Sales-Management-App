package com.tm968.shoppev4.page.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tm968.shoppev4.page.base.BaseResponse
import com.tm968.shoppev4.page.base.ResponseError
import com.tm968.shoppev4.page.repo.Repository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class UserPageViewModel(private val repository: Repository):ViewModel() {
    private val _success = MutableSharedFlow<Any>()
    val success:SharedFlow<Any> get() = _success

    private val _loading = MutableSharedFlow<Any>()
    val loading:SharedFlow<Any> get() = _loading

    fun logout(){
        viewModelScope.launch {
            _loading.emit(true)
            when(val Result = repository.logout()){
                is BaseResponse.Success ->{
                    _success.emit(true)

                }
                is BaseResponse.Error ->{
                    _success.emit(false)
                    _loading.emit(false)
                }
                else -> {}
            }

        }
    }
}