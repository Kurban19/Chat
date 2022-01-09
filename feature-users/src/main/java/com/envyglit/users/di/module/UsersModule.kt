package com.envyglit.users.di.module

import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.users.data.UsersRepositoryImpl
import com.envyglit.users.domain.UsersInteractor
import com.envyglit.users.domain.UsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class UsersModule {

    @Provides
    fun providesUsersRepository(firebaseService: FireBaseService): UsersRepository {
        return UsersRepositoryImpl(firebaseService)
    }

    @Provides
    fun providesUsersInteractor(repository: UsersRepository): UsersInteractor {
        return UsersInteractor(repository)
    }

}

