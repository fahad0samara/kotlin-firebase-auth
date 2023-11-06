package com.fahad.auth_firebase.ui.screen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.fahad.auth_firebase.util.ValidationUtils
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _email = MutableStateFlow("")
    val emailState: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val passwordState: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow("")
    val confirmPasswordState: StateFlow<String> = _confirmPassword

    private val _registrationResponseState = MutableStateFlow<Response<User?>>(Response.Loading)
    val registrationResponseState: StateFlow<Response<User?>> = _registrationResponseState

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError: StateFlow<String?> = _emailError

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError: StateFlow<String?> = _passwordError

    private val _emailInUseError = MutableStateFlow<String?>(null)
    val emailInUseError: StateFlow<String?> = _emailInUseError

    val registrationResult: StateFlow<RegistrationResult> = registrationResponseState.map {
        when (it) {
            is Response.Success -> RegistrationResult.Success(it.data)
            is Response.Failure -> RegistrationResult.Failure(it.e.message ?: "Unknown error")
            is Response.Loading -> RegistrationResult.Loading
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), RegistrationResult.Loading)

    private val emailValue: String
        get() = emailState.value

    private val passwordValue: String
        get() = passwordState.value

    private val confirmPasswordValue: String
        get() = confirmPasswordState.value

    fun setEmail(email: String) {
        _email.value = email
        validateEmail(email)
        checkIfEmailInUse(email)
    }

    fun setPassword(password: String) {
        _password.value = password
        validatePassword(password)
    }

    fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    fun registerUser() {
        if (isRegistrationDataValid()) {
            _registrationResponseState.value = Response.Loading
            viewModelScope.launch {
                _registrationResponseState.value = Response.Loading
                try {
                    val response = authRepository.registerUser(emailValue, passwordValue).first()
                    _registrationResponseState.value = response
                } catch (e: Exception) {
                    _registrationResponseState.value = Response.Failure(e)
                }
            }
        } else {
            _registrationResponseState.value = Response.Failure(Exception("Invalid registration data"))
        }
    }

    private fun isRegistrationDataValid(): Boolean {
        val emailValid = ValidationUtils.isValidEmail(emailValue)
        val passwordValid = ValidationUtils.isValidPassword(passwordValue)
        val confirmPasswordValid = passwordValue == confirmPasswordValue
        val emailNotEmpty = ValidationUtils.isNotEmpty(emailValue)
        val passwordNotEmpty = ValidationUtils.isNotEmpty(passwordValue)
        val confirmPasswordNotEmpty = ValidationUtils.isNotEmpty(confirmPasswordValue)

        _emailError.value = if (!emailValid) "Invalid email format" else null
        _passwordError.value = if (!passwordValid) "Password is too short" else null

        return emailValid && passwordValid && confirmPasswordValid &&
                emailNotEmpty && passwordNotEmpty && confirmPasswordNotEmpty
    }

    private fun validateEmail(email: String) {
        _emailError.value = if (ValidationUtils.isValidEmail(email)) null else "Invalid email format"
    }

    private fun validatePassword(password: String) {
        _passwordError.value = if (ValidationUtils.isValidPassword(password)) null else "Password is too short"
    }

    private fun checkIfEmailInUse(email: String) {
        viewModelScope.launch {
            try {
                authRepository.registerUser(email, "dummyPassword").first()
            } catch (e: Exception) {
                if (e is FirebaseAuthUserCollisionException) {
                    _emailInUseError.value = "Email is already in use"
                }
            }
        }
    }
}




