package com.example.astroviews.di

import com.example.astroviews.database.AppDatabase
import com.example.astroviews.util.ImageLoader
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val koinModule = module {

    single { AppDatabase.getInstance(androidApplication()).theDao() }
    single { ImageLoader(androidContext()) }
}
