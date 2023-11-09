package com.fahad.auth_firebase.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun setUser(userData: User) {
        _user.value = userData
    }

    // Inside UserDataViewModel
// Inside UserDataViewModel
    fun updateUserProfile(displayName: String, photoUri: Uri?) {
        // Update local data
        val currentUser = user.value
        val updatedUser = currentUser?.copy(
            displayName = displayName,
            photoUrl = photoUri?.toString()
        )
        _user.value = updatedUser

        // Update data in Firebase
        val uid = currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                val response = authRepository.updateUserProfile(uid, displayName, photoUri?.toString())
                if (response is Response.Failure) {
                    // Revert local data
                    _user.value = currentUser
                }
            }
        }
    }







    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _user.value = null // Clear local user data
        }
    }
}

