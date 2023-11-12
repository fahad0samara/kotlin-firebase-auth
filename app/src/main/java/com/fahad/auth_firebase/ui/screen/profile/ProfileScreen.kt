package com.fahad.auth_firebase.ui.screen.profile

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.materialIcon

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.fahad.auth_firebase.R


import com.fahad.auth_firebase.ui.UserDataViewModel
import com.fahad.auth_firebase.util.Button.SnackbarWrapper
import kotlin.concurrent.fixedRateTimer


@Composable
fun ProfileScreen(
    navController: NavController, userDataViewModel: UserDataViewModel
) {
    val user by userDataViewModel.user.collectAsState()
    val displayName = user?.displayName
    val email = user?.email
    val photoUrl = user?.photoUrl
    val isEmailVerified = user?.isEmailVerified ?: false

    val successMessage by userDataViewModel.success.collectAsState()
    val errorMessage by userDataViewModel.error.collectAsState()


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)) // Transparent background
            .border(2.dp, Color.Gray, shape = RoundedCornerShape(20.dp)) // Border around the screen
            .padding(7.dp), contentAlignment = Alignment.TopCenter
    ) {
        // Periodically fetch the latest user data
        // Periodically fetch the latest user data with a delay
        DisposableEffect(Unit) {
            val interval = 60 * 1000L // Check every 60 seconds
            val delay = 5000L // Initial delay of 5 seconds
            val timer = fixedRateTimer("emailVerificationTimer", true, delay, interval) {
                userDataViewModel.getUserData()
            }

            onDispose {
                timer.cancel()
            }
        }

        // Display a progress bar when loading
        if (userDataViewModel.isLoading.collectAsState().value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.Center)
            )
        }

        // Display User Information
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(7.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(7.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (!isEmailVerified) {
                // Display a message to verify the email
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(7.dp),
                ) {
                    Text(
                        text = "Verify your email address to unlock all features.",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(7.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    // Button to send email verification
                    Button(
                        onClick = {
                            userDataViewModel.sendEmailVerification()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red
                        )
                    ) {
                        Text("Send Verification Email", color = Color.White)
                    }
                }


            }

            // Display the user's image or a placeholder using CoilImage
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(2.dp, Color.White, CircleShape)
                    .padding(5.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = photoUrl),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape),
                    contentScale = Crop
                )
            }

            // Add a border between image and text
            Spacer(modifier = Modifier.height(8.dp))

            // Display User Name and Email with Icons
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = displayName ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = email ?: "",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit Profile Button
            Button(
                onClick = { navController.navigate("edit_profile") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Edit Profile", color = Color.White)
            }

            // Sign Out Button
            Button(
                onClick = {
                    userDataViewModel.logout()
                    navController.navigate("login")
                },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Sign Out", color = Color.White)
            }
        }
        // Button to send email verification


        // Display Snackbars for success and error messages
        SnackbarWrapper(error = errorMessage, success = successMessage, onDismiss = {
            userDataViewModel.clearError()
            userDataViewModel.clearSuccess()
        })
    }
}







