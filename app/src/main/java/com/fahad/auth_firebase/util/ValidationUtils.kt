package com.fahad.auth_firebase.util

object ValidationUtils {
    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 6 // Adjust the password length requirement as needed
    }

    fun isNotEmpty(value: String): Boolean {
        return value.isNotEmpty()
    }

    // Validation for registration
    fun isRegistrationDataValid(email: String, password: String, confirmPassword: String): Boolean {
        return isValidEmail(email) &&
                isValidPassword(password) &&
                password == confirmPassword &&
                isNotEmpty(email) &&
                isNotEmpty(password) &&
                isNotEmpty(confirmPassword)
    }

    // Validation for login
    fun isLoginDataValid(email: String, password: String): Boolean {
        return isValidEmail(email) &&
                isValidPassword(password) &&
                isNotEmpty(email) &&
                isNotEmpty(password)
    }
}


