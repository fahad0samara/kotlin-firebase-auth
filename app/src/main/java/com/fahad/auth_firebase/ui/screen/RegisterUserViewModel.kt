package com.fahad.auth_firebase.ui.screen

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
class RegisterUserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _registrationResponse = MutableStateFlow<Response<User?>>(Response.Loading)
    val registrationResponse: StateFlow<Response<User?>> = _registrationResponse

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun registerUser() {
        val emailValue = email.value
        val passwordValue = password.value
        val confirmPasswordValue = confirmPassword.value

        if (emailValue.isNotBlank() && passwordValue.isNotBlank() && confirmPasswordValue.isNotBlank() &&
            passwordValue == confirmPasswordValue
        ) {
            _registrationResponse.value = Response.Loading

            viewModelScope.launch {
                try {
                    authRepository.registerUser(emailValue, passwordValue).collect { response ->
                        _registrationResponse.value = response
                    }
                } catch (e: Exception) {
                    _registrationResponse.value = Response.Failure(e)
                }
            }
        } else {
            _registrationResponse.value = Response.Failure(Exception("Invalid registration data"))
        }
    }
}