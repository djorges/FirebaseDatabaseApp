package com.example.firebasedatabase.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

class LoginViewModel(
    private val analytics: FirebaseAnalytics
) : ViewModel(){
    fun showForgotPasswordAction(){
        analytics.logEvent("user_action"){
            param("value1", "Forgot Password")
        }
    }
    fun showRegisterAction(){
        analytics.logEvent("user_action"){
            param("value1", "Don't have an account")
            param("value2", "Register new user")
        }
    }
}