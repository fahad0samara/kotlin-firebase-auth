
package com.fahad.auth_firebase.ui.screen.register

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun RegisterScreen(
    registerViewModel
: RegisterViewModel, navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val registrationResult by registerViewModel.registrationState.collectAsState()


    println(
        "RegisterScreen: registrationResult = $registrationResult"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Name Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            isError = registrationResult is Response.Failure,

            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
        )

        // Email Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            isError = registrationResult is Response.Failure,

            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
        )

        // Password Input
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            isError = registrationResult is Response.Failure,

            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 0.dp, 0.dp, 16.dp)
        )




        // Display error message if registrationResult is a failure
        if (registrationResult is Response.Failure) {
            val error = (registrationResult as Response.Failure).exception
            Text(
                text = "Registration failed: ${error.message}",
                color = Color.Red,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
        }

        // Register Button
        LoadingButton(text = "Register", isLoading = registerViewModel.isLoading, onClick = {

            registerViewModel.registerUser(email, password, name, navController)
        })


    }
}














