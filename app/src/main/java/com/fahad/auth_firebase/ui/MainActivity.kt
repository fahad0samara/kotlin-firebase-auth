package com.fahad.auth_firebase.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fahad.auth_firebase.ui.screen.login.LoginScreen
import com.fahad.auth_firebase.ui.screen.login.LoginViewModel
import com.fahad.auth_firebase.ui.screen.profile.EditProfileScreen
import com.fahad.auth_firebase.ui.screen.profile.ProfileScreen

import com.fahad.auth_firebase.ui.screen.register.RegisterScreen
import com.fahad.auth_firebase.ui.screen.register.RegisterViewModel


import com.fahad.auth_firebase.ui.theme.AuthfirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalComposeUiApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {


            AuthfirebaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {

                    MainScreen()


                }
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val registerViewModel: RegisterViewModel = hiltViewModel()
    val loginViewModel: LoginViewModel = hiltViewModel()
    val userDataViewModel: UserDataViewModel = hiltViewModel()

    // Check authentication state when the MainScreen is recomposed
    LaunchedEffect(Unit) {
        checkAuthenticationState(navController, userDataViewModel)
    }

    NavHost(
        navController = navController,
        startDestination = "loading" // Add a loading destination while checking authentication state
    ) {
        composable("loading") {
            // Show loading screen while checking authentication state
            LoadingScreen()
        }
        composable("login") {
            LoginScreen(
                navController = navController, loginViewModel = loginViewModel
            )
        }
        composable("register") {
            RegisterScreen(
                navController = navController, registerViewModel = registerViewModel
            )
        }
        composable("profile") {
            ProfileScreen(
                navController = navController, userDataViewModel = userDataViewModel
            )
        }
        composable("edit_profile") {
            EditProfileScreen(
                navController = navController, userDataViewModel = userDataViewModel
            )
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

private fun checkAuthenticationState(navController: NavController, userDataViewModel: UserDataViewModel) {
    val auth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null) {
        // Get user data from Firebase
        userDataViewModel.getUserData()


        // Navigate to the success screen
        navController.navigate("profile") {
            popUpTo("loading") { inclusive = true }
        }
    } else {
        // Navigate to the login screen
        navController.navigate("login") {
            popUpTo("loading") { inclusive = true }
        }
    }
}





























