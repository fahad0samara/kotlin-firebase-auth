package com.fahad.auth_firebase.data

import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton




@Singleton
 class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = Firebase.auth.currentUser

    override suspend fun registerUser(email: String, password: String): Flow<Response<User?>> {
        return flow {
            emit(Response.Loading)
            try {
                val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password).await()
                val user = authResult.user
                emit(
                    Response.Success(
                        User(
                            user!!.uid,
                            user.email!!,
                            user.displayName ?: "",
                            user.photoUrl?.toString() ?: ""
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun signIn(email: String, password: String): Flow<Response<User?>> {
        return flow {
            emit(Response.Loading)
            try {
                val authResult = Firebase.auth.signInWithEmailAndPassword(email, password).await()
                val user = authResult.user
                emit(
                    Response.Success(
                        User(
                            user!!.uid,
                            user.email!!,
                            user.displayName ?: "",
                            user.photoUrl?.toString() ?: ""
                        )
                    )
                )
            } catch (e: Exception) {
                emit(Response.Failure(e))
            }
        }.flowOn(Dispatchers.IO)
    }
}






