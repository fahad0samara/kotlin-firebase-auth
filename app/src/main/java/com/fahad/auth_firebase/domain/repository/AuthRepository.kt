package com.fahad.auth_firebase.domain.repository

import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(
        email: String, password: String, displayName: String
    ): Flow<Response<User>>
}
