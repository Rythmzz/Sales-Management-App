package com.tm968.shoppev4.page.base

sealed class BaseResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val exception: Exception) : BaseResponse<Nothing>()
    object Loading : BaseResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "is Loading"
        }
    }
}