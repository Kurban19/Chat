package com.shkiper.chat.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import com.shkiper.chat.firebase.FireBaseChatsImpl
import com.shkiper.chat.repositories.MainRepository
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
open class NetworkModule {


    @Provides
    @Singleton
    fun providesChatsRepository(firebaseChatService: FireBaseChatsImpl): MainRepository {
        return MainRepository(firebaseChatService)
    }

}