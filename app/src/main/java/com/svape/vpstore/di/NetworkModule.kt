package com.svape.vpstore.di

import com.svape.vpstore.data.remote.api.AuthApiService
import com.svape.vpstore.data.remote.api.DataTablesApiService
import com.svape.vpstore.data.remote.api.LocalitiesApiService
import com.svape.vpstore.data.remote.api.VersionApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://apitesting.interrapidisimo.co/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(logging: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideVersionApi(retrofit: Retrofit): VersionApiService =
        retrofit.create(VersionApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideDataTablesApi(retrofit: Retrofit): DataTablesApiService =
        retrofit.create(DataTablesApiService::class.java)

    @Provides
    @Singleton
    fun provideLocalitiesApi(retrofit: Retrofit): LocalitiesApiService =
        retrofit.create(LocalitiesApiService::class.java)
}