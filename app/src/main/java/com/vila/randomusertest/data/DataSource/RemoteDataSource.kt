package com.vila.randomusertest.data.DataSource

import com.vila.randomusertest.data.RandomUserApi
import com.vila.randomusertest.data.dto.toUserList
import com.vila.randomusertest.di.IoDispatcher
import com.vila.randomusertest.domain.models.User
import com.vila.randomusertest.domain.models.WebResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import java.io.IOException
import javax.inject.Inject

class RemoteDataSource @Inject
constructor(
    private val randomUserApi: RandomUserApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    fun getRandomUsers(): Flow<WebResponse<List<User>>> = flow {
        emit(WebResponse.Loading())
        val response = randomUserApi.getRamdomUsers("20")
        emit(WebResponse.Success(response.toUserList()))
    }.flowOn(dispatcher)
        .retryWhen() { cause, attempt ->
            delay(1000)
            return@retryWhen cause is IOException && attempt < 3
        }.catch() { e ->
            emit(WebResponse.Error(e.message!!))

        }
}