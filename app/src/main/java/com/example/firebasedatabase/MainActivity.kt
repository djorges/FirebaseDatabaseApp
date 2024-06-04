package com.example.firebasedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.firebasedatabase.data.util.AuthManager
import com.example.firebasedatabase.data.util.RealtimeManager
import com.example.firebasedatabase.presentation.ui.composable.BottomBar
import com.example.firebasedatabase.presentation.ui.composable.LogoutDialog
import com.example.firebasedatabase.presentation.ui.composable.TopBar
import com.example.firebasedatabase.presentation.ui.screen.ContactsScreen
import com.example.firebasedatabase.presentation.ui.screen.HomeScreen
import com.example.firebasedatabase.presentation.ui.screen.LoginScreen
import com.example.firebasedatabase.presentation.ui.screen.NotesScreen
import com.example.firebasedatabase.presentation.ui.screen.SignUpScreen
import com.example.firebasedatabase.presentation.ui.state.Route
import com.example.firebasedatabase.presentation.ui.theme.FirebaseDatabaseTheme
import com.example.firebasedatabase.presentation.viewmodel.ContactsViewModel
import com.example.firebasedatabase.presentation.viewmodel.CustomViewModelFactory
import com.example.firebasedatabase.presentation.viewmodel.LoginViewModel
import com.example.firebasedatabase.presentation.viewmodel.LoginViewModelFactory
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(
            Firebase.analytics
        )
    }
    private val contactsViewModel: ContactsViewModel by viewModels {
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
                val user = contactsViewModel.currentUser
                var showLogoutDialog by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopBar(user) { showLogoutDialog = true }
                    },
                    bottomBar = {
                        BottomBar(navController = navController)
                    }
                ) {contentPadding ->
                    Box(modifier = Modifier.padding(contentPadding)){
                        //Logout Dialog
                        if(showLogoutDialog){
                            LogoutDialog(
                                onDismiss = {showLogoutDialog = false},
                                onConfirm = {
                                    //Navigate to Login Screen
                                    contactsViewModel.logout()
                                    navController.navigate(Route.Login)

                                    showLogoutDialog = false
                                }
                            )
                        }
                        //Main Content
                        MainContent(
                            navController = navController,
                            contactsViewModel = contactsViewModel,
                            loginViewModel = loginViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent(
    navController: NavHostController,
    contactsViewModel: ContactsViewModel,
    loginViewModel: LoginViewModel
){
    NavHost(
        navController = navController,
        startDestination = Route.Login
    ){
        composable<Route.Login>{
            LoginScreen(
                viewModel = loginViewModel,
                navController = navController
            )
        }
        composable<Route.Home>{
            HomeScreen(navController = navController)
        }
        composable<Route.SignUp>{
            SignUpScreen(navController = navController)
        }
        composable<Route.ForgotPassword>{
            //TODO
        }
        composable<Route.Contacts>{
            ContactsScreen(
                viewModel = contactsViewModel
            )
        }
        composable<Route.Notes> {
            NotesScreen(navController = navController)
        }
    }
}
