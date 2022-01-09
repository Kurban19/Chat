package com.envyglit.chat.di.module

import com.envyglit.chat.data.repository.RepositoryImpl
import com.envyglit.chat.domain.repository.Repository
import com.envyglit.core.data.remote.FireBaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun providesMainRepository(firebaseService: FireBaseService): Repository {
        return RepositoryImpl(firebaseService)
    }

}
