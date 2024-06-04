package com.example.firebasedatabase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.analytics.FirebaseAnalytics

class LoginViewModelFactory(
    private val analytics: FirebaseAnalytics
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(analytics) as T
    }
}