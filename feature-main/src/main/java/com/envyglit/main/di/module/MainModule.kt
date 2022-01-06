package com.envyglit.main.di.module

import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.main.data.MainRepositoryImpl
import com.envyglit.main.domain.MainInteractor
import com.envyglit.main.domain.MainInteractorImpl
import com.envyglit.main.domain.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun providesHomeRepository(firebaseService: FireBaseService): MainRepository {
        return MainRepositoryImpl(firebaseService)
    }

    @Provides
    fun providesHomeInteractor(repository: MainRepository): MainInteractor {
        return MainInteractorImpl(repository)
    }

}
