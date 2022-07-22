package com.tm968.shoppev4.page.utils

object CheckValidData {
    fun isEmailValid (email:String):Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}