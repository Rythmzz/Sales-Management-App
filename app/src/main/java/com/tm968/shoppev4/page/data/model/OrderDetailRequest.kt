package com.tm968.shoppev4.page.data.model

import com.google.gson.annotations.SerializedName

data class OrderDetailRequest(
    @SerializedName("ProductId")
    val ProductId: Int,
    @SerializedName("Amount")
    val amount:Int,
)
