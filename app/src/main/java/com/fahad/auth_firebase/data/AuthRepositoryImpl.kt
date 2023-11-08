package com.fahad.auth_firebase.data

import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.fahad.auth_firebase.util.Button.validateEmailAndPassword
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
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
        val validationResponse = validateEmailAndPassword(email, password)
        if (validationResponse is Response.Failure) {
            emit(validationResponse)
            return@flow
        }
        try {


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

        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Response.Failure(Exception("The email address is already in use by another account.")))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun loginUser(email: String, password: String): Flow<Response<User>> = flow {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = User(
                uid = auth.currentUser?.uid ?: "",
                email = auth.currentUser?.email ?: "",
                displayName = auth.currentUser?.displayName ?: "",
                photoUrl = auth.currentUser?.photoUrl.toString()
            )
            emit(Response.Success(user))
        } catch (e: FirebaseAuthInvalidUserException) {
            emit(Response.Failure(Exception("User not found")))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            emit(Response.Failure(Exception("Invalid credentials"))
            )
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)

}















