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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class AuthRepositoryImpl @Inject constructor() : AuthRepository {
    private val auth = Firebase.auth
    private val storage = FirebaseStorage.getInstance()

    override suspend fun registerUser(
        email: String, password: String,
        displayName: String,
        photoUri: String
    ): Flow<Response<User>> = flow {
        val validationResponse = validateEmailAndPassword(email, password)
        if (validationResponse is Response.Failure) {
            emit(validationResponse)
            return@flow
        }
        try {
            // Register the user with email and password
            auth.createUserWithEmailAndPassword(email, password).await()

            // Upload the image to Firebase Storage
            val uploadedPhotoUrl = uploadImageToFirebaseStorage(Uri.parse(photoUri))

            Log.d("uploadedPhotoUrl", "uploadedPhotoUrl: $uploadedPhotoUrl")
            // After successful registration, set the display name and photo URL
            val user = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .setPhotoUri(Uri.parse(uploadedPhotoUrl))
                .build()
            user?.updateProfile(profileUpdates)?.await()
Log.d("uploadedPhotoUrl", "uploadedPhotoUrl: $uploadedPhotoUrl")
            // Fetch the latest user data from Firebase
            val updatedUser = getUserDataFromFirebase()
            emit(Response.Success(updatedUser))
        } catch (e: FirebaseAuthUserCollisionException) {
            emit(Response.Failure(Exception("The email address is already in use by another account.")))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)




    override suspend fun loginUser(
        email: String, password: String,
    ): Flow<Response<User>> = flow {
        val validationResponse = validateEmailAndPassword(email, password)
        if (validationResponse is Response.Failure) {
            emit(validationResponse)
            return@flow
        }

        try {
            auth.signInWithEmailAndPassword(email, password).await()

            // Fetch the latest user data from Firebase
            val updatedUser = getUserDataFromFirebase()



            emit(Response.Success(updatedUser))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO)


    private suspend fun updateUserPhotoUrl(uid: String, photoUrl: String) {
        try {
            val user = auth.currentUser
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUrl))
                .build()
            user?.updateProfile(profileUpdates)?.await()
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or throw a custom exception
            Log.e("AuthRepositoryImpl", "Error updating user's photoUrl: ${e.message}")
        }
    }

    private suspend fun uploadImageToFirebaseStorage(photoUri: Uri): String {
        return try {
            // Create a reference to the image file in Firebase Storage
            val storageRef = storage.reference
            val imageRef = storageRef.child("profile_images/${photoUri.lastPathSegment}")

            // Upload the image to Firebase Storage
            imageRef.putFile(photoUri).await()

            // Get the download URL
            val downloadUrl = imageRef.downloadUrl.await()

            downloadUrl.toString()
        } catch (e: Exception) {
            // Handle the exception, e.g., log it or throw a custom exception
            Log.e("AuthRepositoryImpl", "Error uploading image to Firebase Storage: ${e.message}")
            ""
        }
    }

    private suspend fun getUserDataFromFirebase(): User {
        val firebaseUser = auth.currentUser
        val uid = firebaseUser?.uid ?: throw Exception("User is not logged in")
        Log.d("AuthRepositoryImpl", "getUserDataFromFirebase - uid: $uid")
        val email = firebaseUser.email ?: ""
        val displayName = firebaseUser.displayName ?: ""
        val photoUrl = firebaseUser.photoUrl?.toString() ?: ""

        return User(uid, email, displayName, photoUrl)
        Log.d("AuthRepositoryImpl", "getUserDataFromFirebase - photoUrl: $photoUrl")







    }



    private suspend fun downloadImageFromFirebaseStorage(photoUrl: String): String {
        return try {
            // Create a reference to the image file in Firebase Storage
            val storageRef = storage.reference
            // Use the photoUrl directly to create the reference
            val imageRef = storageRef.child(photoUrl)

            Log.d("AuthRepositoryImpl", "downloadImageFromFirebaseStorage - photoUrl: $photoUrl")
            // Download the image file and get the download URL
            val downloadUrl = imageRef.downloadUrl.await()
            Log.d("AuthRepositoryImpl", "downloadImageFromFirebaseStorage - downloadUrl: $downloadUrl")

            downloadUrl.toString()
        } catch (e: Exception) {
            // Log the URL that caused the issue
            Log.e("AuthRepositoryImpl", "Error downloading image from Firebase Storage. Photo URL: $photoUrl. Error: ${e.message}")

            // Return an empty string or handle the error as needed
            ""
        }
    }






    override suspend fun getUserData(): Response<User> = flow {
        try {
            val user = getUserDataFromFirebase()
            emit(Response.Success(user))
        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }.flowOn(Dispatchers.IO).single()

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


















