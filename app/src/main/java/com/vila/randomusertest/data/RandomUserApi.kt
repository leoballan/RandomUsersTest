package com.vila.randomusertest.data

import com.vila.randomusertest.data.dto.RandomUserResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {

    @GET("/api")
    suspend fun getRamdomUsersWithseed(@Query("results")quantity:String
                                       , @Query("seed") seed:String):RandomUserResponseDto
    @GET("/api")
    suspend fun getRamdomUsers(@Query("results")quantity:String
                                       ):RandomUserResponseDto

}