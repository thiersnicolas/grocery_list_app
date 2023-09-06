package com.example.grocerylist

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun createAuthInterceptor(tokenSessionManager: TokenSessionManager): Interceptor {
        return AuthInterceptor(tokenSessionManager)
    }

    @Provides
    @Singleton
    fun createTokenSessionManager(): TokenSessionManager {
        return TokenSessionManager()
    }
}
