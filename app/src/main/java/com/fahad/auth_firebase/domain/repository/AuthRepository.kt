package com.fahad.auth_firebase.domain.repository

import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?

    suspend fun registerUser(email: String, password: String): Flow<Response<User?>>
    suspend fun signIn(email: String, password: String): Flow<Response<User?>>
}