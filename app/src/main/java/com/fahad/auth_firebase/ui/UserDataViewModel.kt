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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    val _profileError = MutableStateFlow<String?>(null)
    val profileError: StateFlow<String?> = _profileError

    val _editProfileError = MutableStateFlow<String?>(null)
    val editProfileError: StateFlow<String?> = _editProfileError

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    // Mutable state flows for internal use
    private val _profileSuccess = MutableStateFlow<String?>(null)
    private val _editProfileSuccess = MutableStateFlow<String?>(null)

    // Exposed state flows for external observation
    val profileSuccess: StateFlow<String?> = _profileSuccess
    val editProfileSuccess: StateFlow<String?> = _editProfileSuccess

    private val _isEmailVerified = MutableStateFlow(false)
    val isEmailVerified: StateFlow<Boolean> = _isEmailVerified


    fun getUserData() {
        viewModelScope.launch {
            val response = authRepository.getUserData()
            if (response is Response.Success) {
                _user.value = response.data
                // Check if the user is already verified
                _isEmailVerified.value = response.data.isEmailVerified
            }
        }
    }


    fun markEmailAsVerified() {
        _isLoading.value = true
        _profileSuccess.value = null
        _isEmailVerified.value = false
        _profileError.value = null

        viewModelScope.launch {
            try {
                val response = authRepository.markEmailAsVerified()
                handleResponse(response)

            } catch (e: Exception) {
                _profileError.value = "Failed to mark email as verified: ${e.message}"


            } finally {
                _isLoading.value = false


            }
        }
    }


    fun setUser(userData: User) {
        _user.value = userData
    }

    // In UserDataViewModel
    fun updateUserProfile(displayName: String, photoUri: Uri, navController: NavController) {
        _isLoading.value = true

        _editProfileError.value = null
        _editProfileSuccess.value = null


        // Update data in Firebase
        val currentUser = user.value
        val uid = currentUser?.uid

        if (uid != null) {
            viewModelScope.launch {
                try {
                    val response =
                        authRepository.updateUserProfile(uid, displayName, photoUri.toString())
                    if (response is Response.Success) {
                        // Update local data only if the remote update is successful
                        _user.value = currentUser.copy(
                            displayName = displayName, photoUrl = photoUri.toString()
                        )

                        _editProfileSuccess.value = "Profile updated successfully"
                        // Navigate to the profile screen after the snackbar disappears
                        delay(2000)
                        navController.popBackStack()


                    } else if (response is Response.Failure) {
                        _editProfileError.value =
                            "Failed to update profile: ${response.exception.message}"
                    }
                } catch (e: Exception) {
                    _editProfileError.value = "Failed to update profile: ${e.message}"
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }


    fun clearMessages() {

        _profileError.value = null
        _profileSuccess.value = null
        _editProfileError.value = null
        _editProfileSuccess.value = null
    }


    private fun handleResponse(response: Response<*>) {
        when (response) {
            is Response.Success -> _editProfileSuccess.value = "Success: ${response.data}"
            is Response.Failure -> _profileError.value = "Error: ${response.exception.message}"
            else -> _profileError.value = "Error: Unknown error"
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _user.value = null
        }
    }
}


