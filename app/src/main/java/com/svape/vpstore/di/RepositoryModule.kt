package com.svape.vpstore.di

import com.svape.vpstore.data.repository.AuthRepositoryImpl
import com.svape.vpstore.data.repository.DataTablesRepositoryImpl
import com.svape.vpstore.data.repository.LocalitiesRepositoryImpl
import com.svape.vpstore.data.repository.VersionRepositoryImpl
import com.svape.vpstore.domain.repository.AuthRepository
import com.svape.vpstore.domain.repository.DataTablesRepository
import com.svape.vpstore.domain.repository.LocalitiesRepository
import com.svape.vpstore.domain.repository.VersionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindVersionRepository(impl: VersionRepositoryImpl): VersionRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindDataTablesRepository(impl: DataTablesRepositoryImpl): DataTablesRepository

    @Binds
    @Singleton
    abstract fun bindLocalitiesRepository(impl: LocalitiesRepositoryImpl): LocalitiesRepository
}