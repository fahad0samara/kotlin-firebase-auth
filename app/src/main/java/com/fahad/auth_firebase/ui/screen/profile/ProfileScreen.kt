package com.fahad.auth_firebase.ui.screen.profile

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

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fahad.auth_firebase.ui.UserDataViewModel
import com.fahad.auth_firebase.util.Button.LoadingButton
import com.fahad.auth_firebase.util.Button.SnackbarWrapperProfile


@Composable
fun ProfileScreen(
    navController: NavController, userDataViewModel: UserDataViewModel
) {
    val user by userDataViewModel.user.collectAsState()
    val displayName = user?.displayName
    val email = user?.email
    val photoUrl = user?.photoUrl

    val error = userDataViewModel.profileError.collectAsState().value
    val success = userDataViewModel.profileSuccess.collectAsState().value
    val isLoading = userDataViewModel.isLoading.collectAsState().value
    val isEmailVerified by userDataViewModel.isEmailVerified.collectAsState()

    val largeRadialGradient = MaterialTheme.colorScheme.background.copy(alpha = 0.1f)

    Box(
        modifier = Modifier
            .fillMaxSize()

            .background(largeRadialGradient)
            .padding(10.dp), contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier

                .fillMaxSize()
                .padding(top = 30.dp, bottom = 30.dp)

                .background(MaterialTheme.colorScheme.surface)
                .border(
                    2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(25.dp)
                )
                .fillMaxWidth()

                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (!isEmailVerified) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        Text(
                            text = "Verify your email to get full access" + "so check your email and click on the link to verify your email" + "if you didn't get any email click on the button to resend the email",

                            fontSize = 12.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,

                            )
                        Spacer(modifier = Modifier.height(4.dp))


                        LoadingButton(
                            isLoading = isLoading,
                            text = "Verify Email",

                            onClick = {
                                userDataViewModel.markEmailAsVerified()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            enabled = true,
                            textloading = "we check your email"

                        )


                    }


                }


            }
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

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(10.dp)


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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .padding(10.dp)
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
    }

    SnackbarWrapperProfile(
        success = success,
        error = error,
        onDismiss = {
            userDataViewModel.clearMessages()


        }
    )
}









