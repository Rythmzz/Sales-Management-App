package com.tm968.shoppev4.page.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetail( @SerializedName("orderId")
                        val orderId: Int,
                        @SerializedName("productId")
                        val productId: Int,
                        @SerializedName("amount")
                        val amount: Int,
                        @SerializedName("price")
                        val Price: Double?=0.0,):Parcelable
