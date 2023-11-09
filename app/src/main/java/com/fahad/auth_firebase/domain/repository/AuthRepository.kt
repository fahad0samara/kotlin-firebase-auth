package com.fahad.auth_firebase.domain.repository

import android.net.Uri
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun registerUser(
        email: String, password: String, displayName: String, photoUrl: String
    ): Flow<Response<User>>

    suspend fun loginUser(email: String, password: String): Flow<Response<User>>



    suspend fun updateUserProfile(uid: String, displayName: String, photoUrl: String?): Response<User>
    suspend fun logout(): Response<Unit>
}
