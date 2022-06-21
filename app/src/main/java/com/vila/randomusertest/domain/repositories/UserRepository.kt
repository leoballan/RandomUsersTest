package com.vila.randomusertest.domain.repositories

import com.vila.randomusertest.domain.models.Result
import com.vila.randomusertest.domain.models.User
import com.vila.randomusertest.domain.models.WebResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUsers(): Flow<WebResponse<List<User>>>
}