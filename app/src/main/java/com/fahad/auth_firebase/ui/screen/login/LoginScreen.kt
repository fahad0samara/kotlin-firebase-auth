package com.fahad.auth_firebase.ui.screen.login
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fahad.auth_firebase.domain.model.Response


import com.fahad.auth_firebase.util.Button.LoadingButton
@Composable
fun LoginScreen(
    loginViewModel
    : LoginViewModel, navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val loginResult by loginViewModel.loginState.collectAsState()

    println("Register: loginResult = $loginResult")



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Email Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = loginResult is Response.Failure,
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // Password Input
           OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password
                ),
                isError = loginResult is Response.Failure,
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
            )


// Display error message if loginResult is a failure
    if (loginResult is Response.Failure) {
        Text(
            text = (loginResult as Response.Failure).exception.message ?: "Unknown error",
            color = Color.Red,
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp, 0.dp, 0.dp)
        )
    }




    // Login Button
    LoadingButton(text = "Login", isLoading = loginViewModel.isLoading) {
        loginViewModel.login(email, password, navController)

    }

    // Navigation button to registration screen
    TextButton(
        onClick = { navController.navigate("register") }
    ) {
        Text("Don't have an account? Register here")
    }
}
}

