package com.fahad.auth_firebase.ui.screen.register

import com.fahad.auth_firebase.domain.model.User

sealed class RegistrationResult {
    object Loading : RegistrationResult()
    data class Success(val user: User?) : RegistrationResult()
    data class Failure(val errorMessage: String) : RegistrationResult()
}
