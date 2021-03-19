package ru.skillbranch.chat.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import ru.skillbranch.chat.firebase.FireBaseChatsImpl
import ru.skillbranch.chat.repositories.ChatRepository
import javax.inject.Singleton


@Module
@InstallIn
open class NetworkModule {

    @Provides
    fun providesFireBaseChatsService(): FireBaseChatsImpl{
        return FireBaseChatsImpl()
    }

    @Provides
    @Singleton
    fun providesFireBaseChatsRepository(firebaseChatService: FireBaseChatsImpl): ChatRepository {
        return ChatRepository(firebaseChatService)
    }

}