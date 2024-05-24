package com.example.firebasedatabase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.firebasedatabase.data.util.AuthManager
import com.example.firebasedatabase.data.util.RealtimeManager

class CustomViewModelFactory(
    private val realtimeManager: RealtimeManager,
    private val authManager: AuthManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactsViewModel(realtimeManager, authManager) as T
    }
}