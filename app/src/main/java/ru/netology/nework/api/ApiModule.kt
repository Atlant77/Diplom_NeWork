package ru.netology.nework.api

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.netology.nework.BuildConfig
import ru.netology.nework.auth.AppAuth
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "${BuildConfig.BASE_URL}api/"

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideLogging(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if (BuildConfig.DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun provideAuthInterceptor(
        auth: AppAuth,
    ): Interceptor = Interceptor { chain ->
        val request = auth.authStateFlow.value.token?.let { token ->
            chain.request()
                .newBuilder()
                .addHeader("Authorization", token)
                .build()
        } ?: chain.request()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideOkHttp(
        logging: HttpLoggingInterceptor,
        authInterceptor: Interceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .writeTimeout(30, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.MINUTES)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    fun provideAuthPrefs(
        @ApplicationContext
        context: Context,
    ): SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)


    @Singleton
    @Provides
    fun providePostApi(retrofit: Retrofit): PostApi = retrofit.create()

    @Singleton
    @Provides
    fun provideEventApi(retrofit: Retrofit): EventApi = retrofit.create()

    @Singleton
    @Provides
    fun provideJobsApi(retrofit: Retrofit): JobApi = retrofit.create()

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create()
}
