package com.tm968.shoppev4.page.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val categoryId: Int,
    val description: String,
    val id: Int,
    val image_url: String,
    val name: String,
    val price: Int,
    val sale_price: Int,
    val view: Int
):Parcelable