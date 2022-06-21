package com.vila.randomusertest.di

import com.vila.randomusertest.data.Repositories.UserRepositoryImp
import com.vila.randomusertest.domain.repositories.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun providesRepository(userRepositoryImp: UserRepositoryImp):UserRepository


}