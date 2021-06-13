package com.example.astroviews.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {

    protected val ITEM_PER_PAGE = 20

    private var _viewModelScope: CoroutineScope? = null
    private var _coroutineContext: CoroutineContext? = null

    @VisibleForTesting
    fun setViewModelScope(scope: CoroutineScope) {
        _viewModelScope = scope
    }

    @VisibleForTesting
    fun setCoroutineContext(context: CoroutineContext) {
        _coroutineContext = context
    }
}