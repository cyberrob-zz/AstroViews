package com.example.nasagallery.di

import com.example.nasagallery.viewmodel.FirstViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { FirstViewModel() }
}