package com.rainbow.server.common

import org.springframework.http.HttpStatus

data class CommonResponse<T>(
    var message: String = "ok",
    var data: T? = null,
)

data class ErrorResponse(
    val status: Int,
    val error: HttpStatus,
    val message: String,
)

fun <T> success(data: T? = null): CommonResponse<T> = CommonResponse(data = data)
