package com.fahad.auth_firebase.ui

import android.net.Uri
import android.util.Log
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

    fun updateUserProfile(displayName: String, photoUri: String) {
        // Update local data
        val currentUser = user.value
        val updatedUser = currentUser?.copy(
            displayName = displayName,
            photoUrl = photoUri
        )
        _user.value = updatedUser

        // Update data in Firebase
        val uid = currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                val response = authRepository.updateUserProfile(uid, displayName, photoUri)
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
            _user.value = null
        }
    }
}


