package com.shkiper.chat.di.module

import com.shkiper.chat.data.remote.FireBaseServiceImpl
import com.shkiper.chat.data.remote.FireBaseService
import com.shkiper.chat.data.repository.RepositoryImpl
import com.shkiper.chat.data.local.Database
import com.shkiper.chat.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Provides
    fun providesFireBaseService(): FireBaseService = FireBaseServiceImpl()

    @Provides
    @Singleton
    fun providesMainRepository(firebaseService: FireBaseService, database: Database): Repository {
        return RepositoryImpl(firebaseService, database)
    }


}