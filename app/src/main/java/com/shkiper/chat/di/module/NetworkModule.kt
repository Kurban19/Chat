package com.shkiper.chat.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.shkiper.chat.firebase.FireBaseServiceImpl
import com.shkiper.chat.interfaces.FireBaseService
import com.shkiper.chat.repositories.MainRepository
import dagger.Binds
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    fun providesFireBaseService(): FireBaseService = FireBaseServiceImpl()

    @Provides
    @Singleton
    fun providesMainRepository(firebaseService: FireBaseService): MainRepository {
        return MainRepository(firebaseService)
    }


}