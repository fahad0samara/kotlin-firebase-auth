package com.fahad.auth_firebase.ui.screen.register

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _registrationState = MutableStateFlow<Response<User>>(Response.Loading)
    val registrationState: StateFlow<Response<User>> = _registrationState


    // Property to store the user's display name
    private val _displayName = MutableStateFlow<String>("") // Store the display name
    val displayName: StateFlow<String> = _displayName

    private val _isLoading = mutableStateOf(false)

    val isLoading:Boolean
        get() = _isLoading.value

    fun registerUser(email: String, password: String, displayName: String,navController: NavController) {
        _isLoading.value = true
        viewModelScope.launch {
            _registrationState.value = Response.Loading
            val registrationResult = repository.registerUser(email, password, displayName).first()
            _isLoading.value = false
            if (registrationResult is Response.Success) {
                // Registration successful, update the display name
                _displayName.value = displayName
                navController.navigate("success")
            }



            _registrationState.value = registrationResult
        }
    }

}
