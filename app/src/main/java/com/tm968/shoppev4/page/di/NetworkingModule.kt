package com.tm968.shoppev4.page.di

import com.google.gson.GsonBuilder
import com.tm968.shoppev4.BuildConfig
import com.tm968.shoppev4.page.data.remote.Api
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

val netWorkingModule = module {
    single { provideRetrofit() }
    single { provideApiService(get()) }
}

fun provideRetrofit():Retrofit{
    val gson = GsonBuilder().setLenient().create()
    return Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
fun provideApiService(retrofit: Retrofit):Api{
    return retrofit.create(Api::class.java)
}