package com.example.nasagallery.di

import com.example.nasagallery.viewmodel.FirstViewModel
import com.example.nasagallery.viewmodel.GridViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { FirstViewModel(get()) }
    factory { GridViewModel(get()) }
}