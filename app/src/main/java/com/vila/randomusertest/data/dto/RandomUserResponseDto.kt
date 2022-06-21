package com.vila.randomusertest.data.dto

import com.google.gson.annotations.SerializedName
import com.vila.randomusertest.domain.models.Info
import com.vila.randomusertest.domain.models.Result
import com.vila.randomusertest.domain.models.User

data class RandomUserResponseDto(
    val info: Info,
    val results: List<Result>
)

fun RandomUserResponseDto.toUserList():List<User>{
    val users = results.map { result ->
        User(result.name,
        result.email,
        result.location.coordinates,
        result.dob.age,
        result.picture,
        result.login.username)
    }
    return users
}



