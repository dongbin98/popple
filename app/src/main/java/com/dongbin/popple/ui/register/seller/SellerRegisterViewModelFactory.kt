package com.dongbin.popple.ui.register.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dongbin.popple.data.api.SellerApi

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class SellerRegisterViewModelFactory(private val api: SellerApi) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SellerRegisterViewModel::class.java)) {
            return SellerRegisterViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}