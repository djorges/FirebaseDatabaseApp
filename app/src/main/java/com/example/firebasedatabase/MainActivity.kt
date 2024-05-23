package com.example.firebasedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.firebasedatabase.presentation.ui.screen.ContactsScreen
import com.example.firebasedatabase.presentation.ui.theme.FirebaseDatabaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge mode
        enableEdgeToEdge()

        setContent {
            FirebaseDatabaseTheme {

                ContactsScreen()
            }
        }
    }
}