package com.example.astroviews.di

import com.example.astroviews.viewmodel.FirstViewModel
import com.example.astroviews.viewmodel.GridViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { FirstViewModel(get()) }
    factory { GridViewModel(get()) }
}