package com.rainbow.server.common

import org.springframework.http.HttpStatus

data class CommonResponse<T>(
    var message: String = "ok",
    var data: T? = null,
)

fun <T> success(data: T? = null): CommonResponse<T> = CommonResponse(data = data)
