package com.fahad.auth_firebase.data

import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val auth = Firebase.auth


    override suspend fun registerUser(
        email: String, password: String,
        displayName: String): Flow<Response<User>> = flow {
        try {
            if (password.length < 6) {
                emit(Response.Failure(Exception("Password must be at least 6 characters")))
            } else {
                auth.createUserWithEmailAndPassword(email, password).await()
                emit(
                    Response.Success(
                        User(
                            uid = auth.currentUser?.uid ?: "",
                            email = auth.currentUser?.email ?: "",
                            displayName = auth.currentUser?.displayName,
                            photoUrl = auth.currentUser?.photoUrl.toString()
                        )
                    )
                )
            }
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Response.Failure(Exception("The email address is already in use by another account.")))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

}















