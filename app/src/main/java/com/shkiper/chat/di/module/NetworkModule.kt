package com.shkiper.chat.di.module

import com.shkiper.chat.firebase.FireBaseServiceImpl
import com.shkiper.chat.interfaces.FireBaseService
import com.shkiper.chat.repositories.MainRepository
import com.shkiper.chat.room.Database
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
    fun providesMainRepository(firebaseService: FireBaseService, database: Database): MainRepository {
        return MainRepository(firebaseService, database)
    }


}