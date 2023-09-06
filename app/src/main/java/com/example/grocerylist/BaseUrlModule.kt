package com.example.grocerylist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BaseUrlModule {

    @Provides
    @Singleton
    fun getBaseUrl(): String {
        return "https://grocery-list-webservice.onrender.com"
    }
}
