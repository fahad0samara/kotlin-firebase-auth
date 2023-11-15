package com.fahad.auth_firebase.ui.screen.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.ui.screen.compenets.DisplayError
import com.fahad.auth_firebase.ui.screen.compenets.EmailAndPasswordInputs
import com.fahad.auth_firebase.ui.screen.compenets.NavigationButton


import com.fahad.auth_firebase.util.Button.LoadingButton

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel, navController: NavController
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    val loginResult by loginViewModel.loginState.collectAsState()

    println("Register: loginResult = $loginResult")



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()

            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "welcome to the app",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold


        )

        Spacer(modifier = Modifier.height(50.dp))

        EmailAndPasswordInputs(
            email = email,
            onEmailChange = { email = it },
            password = password,
            onPasswordChange = { password = it },
            isError = loginResult is Response.Failure
        )


// Display error message if loginResult is a failure
        DisplayError(loginResult)


        // Login Button
        LoadingButton(
            text = "Login", isLoading = loginViewModel.isLoading,
            enabled = !(email.isBlank() || password.isBlank()),
            textloading = "login...",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),


            onClick = {
                loginViewModel.login(email, password, navController)
            },
        )


        // Navigation button to registration screen
        NavigationButton(navController = navController)
    }
}









