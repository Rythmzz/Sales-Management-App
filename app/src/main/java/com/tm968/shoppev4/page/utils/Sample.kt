package com.tm968.shoppev4.page.utils

import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sample(val id: Int, val name: String) : Parcelable