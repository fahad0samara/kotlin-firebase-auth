package com.fahad.auth_firebase.di

import com.fahad.auth_firebase.data.AuthRepositoryImpl
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.google.firebase.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }
}