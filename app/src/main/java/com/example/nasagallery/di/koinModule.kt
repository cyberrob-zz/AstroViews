package com.example.nasagallery.di

import com.example.nasagallery.database.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val koinModule = module {

    single { AppDatabase.getInstance(androidApplication()).theDao() }
}
