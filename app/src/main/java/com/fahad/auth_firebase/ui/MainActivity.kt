package com.fahad.auth_firebase.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Surface

import androidx.compose.runtime.Composable

import androidx.compose.runtime.collectAsState

import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.fahad.auth_firebase.ui.screen.register.RegisterScreen
import com.fahad.auth_firebase.ui.screen.register.RegisterViewModel


import com.fahad.auth_firebase.ui.ui.theme.AuthfirebaseTheme
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
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
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

    NavHost(
        navController = navController,
        startDestination = "register"
    ) {
        composable("register") {
            RegisterScreen(navController = navController,
                registerViewModel = registerViewModel
                )

        }
        composable("success") {
            SuccessScreen(navController = navController,
                registerViewModel = registerViewModel
                )
        }

    }
}


@Composable
fun SuccessScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel
){

    val displayName = registerViewModel.displayName.collectAsState(initial = "").value


    println(
           "SuccessScreenprintln: displayName = $displayName"
    )

    print("SuccessScreenprint: displayName = $displayName")
    Log.d("SuccessScreenLog: displayName = $displayName", "SuccessScreenLog: displayName = $displayName")






    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


    }
}






















