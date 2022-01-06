package com.envyglit.main.di.module

import com.envyglit.core.data.local.Database
import com.envyglit.core.data.remote.FireBaseService
import com.envyglit.main.domain.MainInteractor
import com.envyglit.main.domain.MainInteractorImpl
import com.envyglit.main.domain.MainRepository
import com.envyglit.main.util.mock.MainMockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun providesHomeRepository(firebaseService: FireBaseService, database: Database): MainRepository {
        return MainMockRepository()
    }

    @Provides
    fun providesHomeInteractor(repository: MainRepository): MainInteractor {
        return MainInteractorImpl(repository)
    }

}
