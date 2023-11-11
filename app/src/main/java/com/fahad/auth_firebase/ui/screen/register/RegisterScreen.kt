
package com.fahad.auth_firebase.ui.screen.register

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.util.Button.LoadingButton

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    navController: NavController
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    val registrationResult by registerViewModel.registrationState.collectAsState()


    println(
        "println $registrationResult"
    )

    // Get the result of the image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,

    ) {
        // Center the image and show an icon if no image is selected
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // Display the selected image or show an icon
            if (photoUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = "User's photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Black, CircleShape)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "No photo selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(80.dp).align(Alignment.Center).border(2.dp, Color.Yellow, CircleShape)
                )
            }
        }

        // Button to open the image picker
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { launcher.launch("image/*") },

                modifier = Modifier.padding(16.dp)
            ) {
                Text("Select Photo")
            }

            // Button to clear the selected image
            if (photoUri != null) {
                Button(
                    onClick = { photoUri = null },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.secondary, shape = CircleShape)
                ) {
                    Text("Clear")
                }
            }
        }

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

        // Spacer to create some space between input fields and button
        Spacer(modifier = Modifier.height(16.dp))

        // Display error message if loginResult is a failure
        if (registrationResult is Response.Failure) {
            val error = (registrationResult as Response.Failure).exception
            Log.d("RegisterScreen", "Registration failed: ${error.message}")
            Text(
                text = "Registration failed: ${error.message}",
                color = Color.Red,
                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 16.dp)
            )
        }
        // Register Button
        LoadingButton(
            text = "Register",
            isLoading = registerViewModel.isLoading,
            onClick = {
                registerViewModel.registerUser(email, password, name, photoUri.toString(), navController)
            }
        )
    }
}

















