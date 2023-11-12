package com.fahad.auth_firebase.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.fahad.auth_firebase.R
import com.fahad.auth_firebase.ui.UserDataViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    userDataViewModel: UserDataViewModel
) {


    var displayName by rememberSaveable { mutableStateOf(userDataViewModel.user.value?.displayName ?: "") }
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(
        userDataViewModel.user.value?.photoUrl?.let { Uri.parse(it


        ) }
    ) }



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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display the selected image or show an icon
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = // Placeholder image resource
                rememberAsyncImagePainter(
                    ImageRequest.Builder // Error image resource
                    (LocalContext.current).data(
                        photoUri
                    ).apply(block = fun ImageRequest.Builder.() {
                    placeholder(R.drawable.ic_launcher_background) // Placeholder image resource
                    error(R.drawable.ic_launcher_foreground) // Error image resource
                    crossfade(true)
                }).build()
                ),
                contentDescription = "User Image",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)

                    .padding(vertical = 16.dp)
            )
        }

        // Button to open the image picker
        Button(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Select Photo")
        }

        // Display Name Input
        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            label = { Text("Display Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 16.dp, 0.dp, 16.dp)
        )

        // Save Changes Button

        Button(
            onClick = {
                // Save changes to the user's profile
                photoUri?.let { uri ->
                    userDataViewModel.updateUserProfile(
                        displayName,
                        uri
                    )
                }
                navController.popBackStack()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Save Changes")
        }

    }
}
