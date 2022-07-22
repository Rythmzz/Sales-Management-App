package com.tm968.shoppev4.page.di

import com.tm968.shoppev4.page.di.MyApplication
import com.securepreferences.SecurePreferences
import com.tm968.shoppev4.page.data.local.AppPreferences
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val DataModule = module {

    single { providedSecurePreferences(androidApplication() as MyApplication) }
    single { providedAppSecurePreferences(get()) }

}

fun providedSecurePreferences(app:MyApplication):SecurePreferences{
    return SecurePreferences(app,"","miretty.xml")
}

fun providedAppSecurePreferences (sharedPreferences: SecurePreferences) : AppPreferences{
    return AppPreferences(sharedPreferences)
}