package com.tm968.shoppev4.page.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    @SerializedName("id")
    val id: Int,
    @SerializedName("userId")
    val userId:Int,
    @SerializedName("orderDate")
    val orderDate:String,
    @SerializedName("orderCode")
    val orderCode: String,
    @SerializedName("addressDelivery")
    val addressDelivery: String,
    @SerializedName("orderNote")
    val orderNote: String?,
    @SerializedName("orderDetails")
    val orderDetails: List<OrderDetail>,
):Parcelable