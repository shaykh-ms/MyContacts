package com.example.mycontacts.di

import com.example.mycontacts.data.remote.ContactService
import com.example.mycontacts.data.repository.ContactRepositoryImpl
import com.example.mycontacts.domain.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideContactRepository(
        mealApi: ContactService,
    ): ContactRepository {
        return ContactRepositoryImpl(mealApi)
    }
}