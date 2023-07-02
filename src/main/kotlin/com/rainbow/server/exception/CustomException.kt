package com.rainbow.server.exception

import org.springframework.http.HttpStatus

open class CustomException(
    val errorCode: ErrorCode
): RuntimeException()

enum class ErrorCode(val status: HttpStatus, val message: String) {
    // 400 - Bad Request
    FILE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 갯수 제한을 초과했습니다."),
}
