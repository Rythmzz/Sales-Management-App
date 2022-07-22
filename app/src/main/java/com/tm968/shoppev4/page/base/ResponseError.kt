package com.tm968.shoppev4.page.base

import java.io.Serializable


data class ResponseError (
    val code:Int,
    val msg:String
) : RuntimeException(),Serializable