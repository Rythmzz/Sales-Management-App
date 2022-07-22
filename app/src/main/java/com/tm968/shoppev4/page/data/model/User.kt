package com.tm968.shoppev4.page.data.model



data class User(
    val accessToken: String,
    val email: String,
    val expires: Int,
    val fullName: String,
    val id: Int,
    val password: String,
    val phoneNumber: String,
    val userName: String
)