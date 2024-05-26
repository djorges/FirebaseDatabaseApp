package com.example.firebasedatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.firebasedatabase.data.util.AuthManager
import com.example.firebasedatabase.data.util.RealtimeManager
import com.example.firebasedatabase.presentation.ui.screen.ContactsScreen
import com.example.firebasedatabase.presentation.ui.screen.LoginScreen
import com.example.firebasedatabase.presentation.ui.screen.NotesScreen
import com.example.firebasedatabase.presentation.ui.state.Route
import com.example.firebasedatabase.presentation.ui.theme.FirebaseDatabaseTheme
import com.example.firebasedatabase.presentation.viewmodel.ContactsViewModel
import com.example.firebasedatabase.presentation.viewmodel.CustomViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: ContactsViewModel by viewModels {
        CustomViewModelFactory(
            RealtimeManager(this),
            AuthManager(this)
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge mode
        enableEdgeToEdge()

        setContent {
            FirebaseDatabaseTheme {
                val navController = rememberNavController()
                val user = viewModel.currentUser
                var showLogoutDialog by remember { mutableStateOf(false) }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                //User Image
                                Row (
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    if(user?.photoUrl != null){
                                        AsyncImage(
                                            modifier = Modifier
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            model = ImageRequest.Builder(LocalContext.current),
                                            contentDescription = "User Photo",
                                            placeholder = painterResource(id = R.drawable.baseline_person_search_24),
                                            contentScale = ContentScale.Crop
                                        )
                                    }else{
                                        Image(
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .size(40.dp)
                                                .clip(CircleShape),
                                            painter = painterResource(id = R.drawable.baseline_person_24),
                                            contentDescription = "Default User Photo"
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    //User Name
                                    Column {
                                        Text(
                                            text = if(!user?.displayName.isNullOrEmpty()) "Hello ${user?.displayName}" else "Welcome",
                                            fontSize = 20.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = if(!user?.email.isNullOrEmpty()) "${user?.email}" else "Anonymous",
                                            fontSize = 12.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis)
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(),
                            actions = {
                                IconButton(
                                    onClick = {showLogoutDialog = true}
                                ) {
                                    Icon(Icons.Outlined.ExitToApp, contentDescription = "Logout")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry.fromRoute()

                        //Bottom Navigation
                        NavigationBar(
                            modifier = Modifier
                        ){
                           /* NavigationBarItem(
                                label = {Text(text = ContactsDestination::class.simpleName!!)},
                                icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null)},
                                selected = currentRoute == ContactsDestination,
                                onClick = {
                                    navController.navigate(ContactsDestination)
                                }
                            )*/
                        }
                    }
                ) {contentPadding ->
                    Box(modifier = Modifier.padding(contentPadding)){
                        //Logout Dialog
                        if(showLogoutDialog){
                            LogoutDialog(
                                onDismiss = {showLogoutDialog = false},
                                onConfirm = {
                                    viewModel.logout()
                                    navController.navigate(Route.Contacts)

                                    showLogoutDialog = false
                                }
                            )
                        }
                        //Main Content
                        MainContent(
                            navController = navController,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
fun NavBackStackEntry?.fromRoute(): Route {
    this?.destination?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let {
            when (it) {
                Route.Contacts::class.simpleName -> return Route.Contacts
                Route.Notes::class.simpleName -> return Route.Notes("")
                else -> return Route.Contacts
            }
        }
    return Route.Contacts
}
@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    AlertDialog(
        title = {Text(text = "Close Session")},
        text = {Text(text = "Are you sure you want to close the session?")},
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
fun TopBar(

){

}

@Composable
fun MainContent(
    navController: NavHostController,
    viewModel: ContactsViewModel
){
    NavHost(
        navController = navController,
        startDestination = Route.Login
    ){
        composable<Route.Login>{
            LoginScreen()
        }
        composable<Route.Contacts>{
            ContactsScreen(
                viewModel = viewModel
            )
        }
        composable<Route.Notes> {
            NotesScreen(navController = navController)
        }
    }
}
