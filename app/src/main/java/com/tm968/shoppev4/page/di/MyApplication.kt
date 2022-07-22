package com.tm968.shoppev4.page.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@MyApplication)
            koin.loadModules(arrayListOf(RepositoryModule, viewModelModule, netWorkingModule,
                DataModule))
        }
    }
}