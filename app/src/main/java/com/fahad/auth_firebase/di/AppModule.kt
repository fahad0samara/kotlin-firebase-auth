package com.fahad.auth_firebase.di

import androidx.lifecycle.ViewModelProvider
import com.fahad.auth_firebase.data.AuthRepositoryImpl
import com.fahad.auth_firebase.domain.repository.AuthRepository
import com.fahad.auth_firebase.ui.UserDataViewModel
import com.google.firebase.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }


    @Provides
    @Singleton
    fun provideUserDataViewModel(): UserDataViewModel {
        return UserDataViewModel(
            authRepository = AuthRepositoryImpl()
        )
    }
}