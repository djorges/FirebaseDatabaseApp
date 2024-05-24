package com.example.firebasedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasedatabase.data.util.AuthManager
import com.example.firebasedatabase.data.util.RealtimeManager
import com.example.firebasedatabase.presentation.ui.screen.ContactsScreen
import com.example.firebasedatabase.presentation.ui.screen.NotesScreen
import com.example.firebasedatabase.presentation.ui.state.ContactsDestination
import com.example.firebasedatabase.presentation.ui.state.NotesDestination
import com.example.firebasedatabase.presentation.ui.theme.FirebaseDatabaseTheme
import com.example.firebasedatabase.presentation.viewmodel.ContactsViewModel
import com.example.firebasedatabase.presentation.viewmodel.CustomViewModelFactory
import com.google.android.gms.auth.api.Auth

class MainActivity : ComponentActivity() {

    private val viewModel: ContactsViewModel by viewModels {
        CustomViewModelFactory(
            RealtimeManager(this),
            AuthManager(this)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge mode
        enableEdgeToEdge()

        setContent {
            FirebaseDatabaseTheme {
                val navController = rememberNavController()

                BottomNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    viewModel: ContactsViewModel
){
    NavHost(
        navController = navController,
        startDestination = ContactsDestination
    ){
        composable<ContactsDestination>{
            ContactsScreen(viewModel = viewModel)
        }
        composable<NotesDestination> {
            NotesScreen()
        }
    }
}