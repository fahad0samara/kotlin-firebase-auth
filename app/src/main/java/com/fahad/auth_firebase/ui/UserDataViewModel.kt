package com.fahad.auth_firebase.ui

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    private val _success = MutableStateFlow<String?>(null)
    val success: StateFlow<String?> = _success

    fun getUserData() {
        viewModelScope.launch {
            val response = authRepository.getUserData()
            Log.d("response", "getUserData: $response")
            if (response is Response.Success) {
                _user.value = response.data
            }
        }
    }

    fun setUser(userData: User) {
        _user.value = userData
    }

    // In UserDataViewModel
    fun updateUserProfile(displayName: String, photoUri: Uri, navController: NavController) {
        _isLoading.value = true
        // Update local data
        val currentUser = user.value
        val updatedUser = currentUser?.copy(
            displayName = displayName, photoUrl = photoUri.toString()
        )
        _user.value = updatedUser

        // Update data in Firebase
        val uid = currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                try {
                    val response =
                        authRepository.updateUserProfile(uid, displayName, photoUri.toString())
                    if (response is Response.Success) {
                        _success.value = "Profile updated successfully"
                    } else if (response is Response.Failure) {
                        _error.value = "Failed to update profile: ${response.exception.message}"
                    }
                } catch (e: Exception) {
                    _error.value = "Failed to update profile: ${e.message}"
                } finally {
                    _isLoading.value = false

                    // Delay for 2 seconds before navigating to the profile screen
                    delay(2000)

                    // Navigate to the profile screen
                    navController.navigate("profile") {
                        popUpTo("edit_profile") { inclusive = true }
                    }

                }


            }
        }
    }


    // Clear error and success messages
    fun clearError() {
        _error.value = null
    }

    fun clearSuccess() {
        _success.value = null
    }


    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _user.value = null
        }
    }
}


