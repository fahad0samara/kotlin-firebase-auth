package com.fahad.auth_firebase.ui.screen.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.fahad.auth_firebase.R


import com.fahad.auth_firebase.ui.UserDataViewModel






@Composable
fun ProfileScreen(
    navController: NavController,
    userDataViewModel: UserDataViewModel
) {
    val user by userDataViewModel.user.collectAsState()
    val displayName = user?.displayName
    val email = user?.email
    val photoUrl = user?.photoUrl
Log.d("photoUrl", "photoUrl: $photoUrl")

    Log.d("user", "displayName: $user")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display User Information
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome $displayName",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Your email is $email",
                style = MaterialTheme.typography.labelLarge
            )

            // Display the user's image or a placeholder using CoilImage
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(vertical = 16.dp)
            ) {
                Image(
                    painter = // Placeholder image resource
                    rememberAsyncImagePainter(ImageRequest.Builder // Error image resource
                        (LocalContext.current).data(data = photoUrl).apply(block = fun ImageRequest.Builder.() {
                        placeholder(R.drawable.ic_launcher_background) // Placeholder image resource
                        error(R.drawable.ic_launcher_background) // Error image resource
                        crossfade(true)
                    }).build()
                    ),
                    contentDescription = "User Image",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(vertical = 16.dp)
                )
            }






                // Edit Profile Button
            Button(
                onClick = { /* Navigate to the edit profile screen */
                    navController.navigate("edit_profile")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Edit Profile")
            }

            // Sign Out Button
            Button(
                onClick = {
                    userDataViewModel.logout()
                    navController.navigate("login")
                },
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}
