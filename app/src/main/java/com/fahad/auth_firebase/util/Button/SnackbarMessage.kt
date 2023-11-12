package com.fahad.auth_firebase.util.Button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun SnackbarWrapper(
    error: String?,
    success: String?,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show the Snackbar when error or success message changes
    LaunchedEffect(error, success) {
        if (error != null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short // Set a short duration
                )
            }
        } else if (success != null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = success,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short // Set a short duration
                )
            }
        }
    }

    // Define the background color based on whether it's an error or success
    val backgroundColor = if (error != null) {
        Color.Red
    } else if (success != null) {
        Color.Green
    } else {
        Color.White
    }

    // SnackbarHost that displays the Snackbar
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) { data ->
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .background(backgroundColor), // Apply the background color
            action = {
                if (error != null) {
                    TextButton(onClick = onDismiss) {
                        Text(text = "Dismiss")
                    }
                }
            }
        ) {
            Text(
                text = error ?: success!!, // Display the error or success message

            )
        }
    }
}





