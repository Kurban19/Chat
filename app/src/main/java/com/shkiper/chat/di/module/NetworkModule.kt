package com.shkiper.chat.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.shkiper.chat.firebase.FireBaseServiceImpl
import com.shkiper.chat.repositories.MainRepository
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesMainRepository(firebaseService: FireBaseServiceImpl): MainRepository {
        return MainRepository(firebaseService)
    }

}