package com.fahad.auth_firebase.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _loginResponse = MutableStateFlow<Response<User?>>(Response.Loading)
    val loginResponse: StateFlow<Response<User?>> = _loginResponse

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun login() {
        val emailValue = email.value
        val passwordValue = password.value

        if (emailValue.isNotBlank() && passwordValue.isNotBlank()) {
            // Set the loading state
            _loginResponse.value = Response.Loading

            viewModelScope.launch {
                try {
                    authRepository.signIn(emailValue, passwordValue).collect { response ->
                        _loginResponse.value = response
                    }
                } catch (e: Exception) {
                    _loginResponse.value = Response.Failure(e)
                }
            }
        } else {
            // Handle the case when email or password is blank
            _loginResponse.value = Response.Failure(Exception("Email and password are required"))
        }
    }
}



















