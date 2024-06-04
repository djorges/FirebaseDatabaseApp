package com.example.firebasedatabase.presentation.ui.composable

import com.example.firebasedatabase.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseUser

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBar(
    user: FirebaseUser?,
    onShowLogoutDialog: () -> Unit
) {
    TopAppBar(
        title = {
            //User Image
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (user?.photoUrl != null) {
                    AsyncImage(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        model = ImageRequest.Builder(LocalContext.current),
                        contentDescription = "User Photo",
                        placeholder = painterResource(id = R.drawable.baseline_person_search_24),
                        contentScale = ContentScale.Crop
                    )
                } else {
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
                        text = if (!user?.displayName.isNullOrEmpty()) "Hello ${user?.displayName}" else "Welcome",
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (!user?.email.isNullOrEmpty()) "${user?.email}" else "Anonymous",
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(),
        actions = {
            IconButton(
                onClick = onShowLogoutDialog
            ) {
                Icon(Icons.Outlined.ExitToApp, contentDescription = "Logout")
            }
        }
    )
}