package com.example.firebasedatabase.presentation.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.firebasedatabase.presentation.ui.theme.Purple40
import com.example.firebasedatabase.presentation.viewmodel.LoginViewModel
import com.google.firebase.analytics.logEvent

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    //Login Form
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Login",
            style = TextStyle(
                fontSize = 30.sp,
                fontFamily = FontFamily.Default,
                color = Purple40
            )
        )
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            value = email,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            onValueChange = { email = it },
            label = { Text( text = "Email") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = password,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { password = it },
            label = { Text( text = "Password") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    //TODO: Navigate to Home

                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ){
                Text(text = "Login")
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = "Forgot Password?",
            modifier = Modifier
                .clickable {
                    //
                    viewModel.showForgotPasswordAction()
                },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Don't have an account?. Register",
            modifier = Modifier
                .padding(20.dp)
                .clickable(
                    onClick = {
                        //
                        viewModel.showRegisterAction()
                    }
                )
            ,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = Purple40
            )
        )
    }
}
