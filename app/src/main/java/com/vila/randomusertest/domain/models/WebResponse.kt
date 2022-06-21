package com.vila.randomusertest.domain.models


sealed class WebResponse<out T>{
    data class Success<out T>(val data: T) : WebResponse<T>()
    data class Error(val exception: String) : WebResponse<Nothing>()
    class Loading : WebResponse<Nothing>()
    class Init : WebResponse<Nothing>()
}
