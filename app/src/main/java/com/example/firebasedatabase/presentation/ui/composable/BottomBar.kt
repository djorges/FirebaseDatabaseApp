package com.example.firebasedatabase.presentation.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.firebasedatabase.presentation.ui.state.Route

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.fromRoute()

    //Bottom Navigation
    NavigationBar(
        modifier = modifier
    ) {
        NavigationBarItem(
            label = { Text(text = Route.Contacts::class.simpleName!!) },
            icon = { Icon(imageVector = Icons.Outlined.Person, contentDescription = null) },
            selected = currentRoute == Route.Contacts,
            onClick = {
                navController.navigate(Route.Contacts)
            }
        )
        NavigationBarItem(
            label = { Text(text = Route.Notes::class.simpleName!!) },
            icon = { Icon(imageVector = Icons.Outlined.List, contentDescription = null) },
            selected = currentRoute == Route.Notes,
            onClick = {
                navController.navigate(Route.Notes)
            }
        )
    }
}

fun NavBackStackEntry?.fromRoute(): Route {
    this?.destination?.route?.substringBefore("?")?.substringBefore("/")
        ?.substringAfterLast(".")?.let {
            when (it) {
                Route.Contacts::class.simpleName -> return Route.Contacts
                Route.Notes::class.simpleName -> return Route.Notes
                else -> return Route.Contacts
            }
        }
    return Route.Contacts
}