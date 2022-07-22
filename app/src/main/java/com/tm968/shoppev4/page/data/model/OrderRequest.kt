package com.tm968.shoppev4.page.data.model

import com.google.gson.annotations.SerializedName

data class OrderRequest (
    @SerializedName("UserId")
    val userId:Int,
    @SerializedName("OrderCode")
    val orderCode: String,
    @SerializedName("AddressDelivery")
    val address:String,
    @SerializedName("OrderNode")
    val orderNode:String,
    @SerializedName("OrderDetails")
    val OrderDetails: List<OrderDetailRequest>
    )