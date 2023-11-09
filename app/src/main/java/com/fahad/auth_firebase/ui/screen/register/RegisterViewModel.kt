package com.fahad.auth_firebase.ui.screen.register

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.fahad.auth_firebase.ui.UserDataViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository, private val userDataViewModel: UserDataViewModel
) : ViewModel() {
    private val _registrationState = MutableStateFlow<Response<User>>(Response.Loading)
    val registrationState: StateFlow<Response<User>> = _registrationState

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading.value


    fun registerUser(
        email: String, password: String, displayName: String, photoUrl: String?, // Make it nullable
        navController: NavController
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            _registrationState.value = Response.Loading
            try {
                val registrationResult = repository.registerUser(
                    email, password, displayName, photoUrl.orEmpty()
                ).first()
                _isLoading.value = false

                if (registrationResult is Response.Success) {
                    val user = registrationResult.data
                    userDataViewModel.setUser(user)
                    navController.navigate("success")
                } else if (registrationResult is Response.Failure) {
                    _registrationState.value = registrationResult

                }
            } catch (e: Exception) {
                Log.e("ViewModel", "registerUser: ${e.message}")
                _registrationState.value = Response.Failure(Exception("Something went wrong"))


            }
        }

    }
}

