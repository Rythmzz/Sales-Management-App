package com.tm968.shoppev4.page.di

import com.tm968.shoppev4.page.repo.Repository
import org.koin.dsl.module

val RepositoryModule = module {
    single { Repository(get()) }
}