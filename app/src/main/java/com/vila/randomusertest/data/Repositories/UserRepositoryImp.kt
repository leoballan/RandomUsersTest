package com.vila.randomusertest.data.Repositories

import com.vila.randomusertest.data.DataSource.RemoteDataSource
import com.vila.randomusertest.domain.models.Result
import com.vila.randomusertest.domain.models.User
import com.vila.randomusertest.domain.models.WebResponse
import com.vila.randomusertest.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImp @Inject
constructor(private val remoteDataSource: RemoteDataSource): UserRepository {
    override suspend fun getUsers(): Flow<WebResponse<List<User>>> {
        return remoteDataSource.getRandomUsers()
    }
}