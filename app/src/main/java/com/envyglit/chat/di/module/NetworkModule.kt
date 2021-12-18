package com.envyglit.chat.di.module

import com.envyglit.chat.data.remote.FireBaseServiceImpl
import com.envyglit.core.data.remote.FireBaseService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesFireBaseService(): FireBaseService = FireBaseServiceImpl()

}