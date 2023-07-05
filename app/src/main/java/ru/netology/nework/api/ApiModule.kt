package ru.netology.nework.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.netology.nework.auth.AppAuth
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun providePostApi(auth: AppAuth):PostApi = retrofit(okhttp(loggingInterceptor(), authInterceptor(auth))).create(PostApi::class.java)

    @Singleton
    @Provides
    fun provideUserApi(auth: AppAuth):UserApi = retrofit(okhttp(loggingInterceptor(), authInterceptor(auth))).create(UserApi::class.java)
}