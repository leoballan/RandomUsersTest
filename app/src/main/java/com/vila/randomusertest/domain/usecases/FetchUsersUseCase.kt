package com.vila.randomusertest.domain.usecases

import com.vila.randomusertest.domain.models.User
import com.vila.randomusertest.domain.models.WebResponse
import com.vila.randomusertest.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke() : Flow<WebResponse<List<User>>> {
        return repository.getUsers()
    }
}