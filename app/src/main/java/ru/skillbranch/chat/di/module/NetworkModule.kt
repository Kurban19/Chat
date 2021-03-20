package ru.skillbranch.chat.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ru.skillbranch.chat.firebase.FireBaseChatsImpl
import ru.skillbranch.chat.repositories.ChatRepository
import javax.inject.Singleton


@Module
@InstallIn(ActivityComponent::class)
open class NetworkModule {

    @Provides
    @Singleton
    fun providesFireBaseChatsService(): FireBaseChatsImpl{
        return FireBaseChatsImpl()
    }

    @Provides
    @Singleton
    fun providesFireBaseChatsRepository(firebaseChatService: FireBaseChatsImpl): ChatRepository {
        return ChatRepository(firebaseChatService)
    }

}