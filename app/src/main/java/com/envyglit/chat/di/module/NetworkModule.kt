package com.envyglit.chat.di.module

import com.envyglit.chat.data.remote.FireBaseServiceImpl
import com.envyglit.chat.data.remote.FireBaseService
import com.envyglit.chat.data.repository.RepositoryImpl
import com.envyglit.chat.data.local.Database
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.chat.util.MockRepository
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
        return MockRepository()
    }

}