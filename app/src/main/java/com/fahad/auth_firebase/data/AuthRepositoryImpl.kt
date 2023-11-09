package com.fahad.auth_firebase.data

import android.net.Uri
import android.util.Log
import com.fahad.auth_firebase.domain.model.Response
import com.fahad.auth_firebase.domain.model.User
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.fahad.auth_firebase.util.Button.validateEmailAndPassword
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val auth = Firebase.auth

    override suspend fun registerUser(
        email: String, password: String,
        displayName: String,
        photoUrl: String
    ): Flow<Response<User>> = flow {
        val validationResponse = validateEmailAndPassword(email, password)
        if (validationResponse is Response.Failure) {
            emit(validationResponse)
            return@flow
        }
        try {
            // Register the user with email and password
            auth.createUserWithEmailAndPassword(email, password).await()

            // After successful registration, set the display name
            val user = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(photoUrl))
                .build()
            user?.updateProfile(profileUpdates)?.await()

            // Fetch the latest user data from Firebase
            val updatedUser = getUserDataFromFirebase()
            emit(Response.Success(updatedUser))
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Response.Failure(Exception("The email address is already in use by another account.")))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun loginUser(email: String, password: String): Flow<Response<User>> = flow {
        val validationResponse = validateEmailAndPassword(email, password)
        if (validationResponse is Response.Failure) {
            emit(validationResponse)
            return@flow
        }

        try {
            auth.signInWithEmailAndPassword(email, password).await()
            val user = getUserDataFromFirebase() // Fetch the latest user data from Firebase
            emit(Response.Success(user))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)


    private fun getUserDataFromFirebase(): User {
        val firebaseUser = auth.currentUser
        Log.d("AuthRepositoryImpl", "getUserDataFromFirebase: ${firebaseUser?.photoUrl}"+
                " ${firebaseUser?.displayName} ${firebaseUser?.email} ${firebaseUser?.uid}"


        )
        return User(
            uid = firebaseUser?.uid ?: "",
            email = firebaseUser?.email ?: "",
            displayName = firebaseUser?.displayName ?: "",
            photoUrl = firebaseUser?.photoUrl?.toString() )







    }







    override suspend fun updateUserProfile(
        uid: String,
        displayName: String,
        photoUrl: String?
    ): Response<User> = flow {
        try {
            val user = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(photoUrl))
                .build()
            user?.updateProfile(profileUpdates)?.await()

            // Fetch the updated user data from Firebase
            val updatedUser = getUserDataFromFirebase()
            emit(Response.Success(updatedUser))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO).single()






    override suspend fun logout(): Response<Unit> = flow {
        try {
            auth.signOut()
            emit(Response.Success(Unit))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO).single()


}















