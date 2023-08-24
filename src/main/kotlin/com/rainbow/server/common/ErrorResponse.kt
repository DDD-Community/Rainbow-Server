package com.rainbow.server.common

import org.springframework.http.HttpStatus

data class ErrorResponse(
    val status: Int,
    val error: HttpStatus,
    val message: String,
)
