
package com.rainbow.server.exception

import com.rainbow.server.common.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException

const val uploadLimitMessage = "파일 업로드 용량 제한을 초과했습니다."

open class CustomException(
    val errorCode: ErrorCode,
    var entity: String? = null,
) : RuntimeException()

enum class ErrorCode(val status: HttpStatus, val message: String) {
    FILE_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST, "파일 갯수 제한을 초과했습니다."), // 400 - Bad Request
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "객체가 존재하지 않습니다."), // 404 - Not Found
    INVALID_INPUT_FILE(HttpStatus.BAD_REQUEST, "파일이 손상되었습니다."), // 400 - Bad Request
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(
            status = e.errorCode.status.value(),
            error = HttpStatus.BAD_REQUEST,
            message = e.errorCode.message,
            entity = e.entity,
        )
        return ResponseEntity.status(e.errorCode.status).body(errorResponse)
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.PAYLOAD_TOO_LARGE.value(),
            error = HttpStatus.PAYLOAD_TOO_LARGE,
            message = uploadLimitMessage,
        )
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE.value()).body(errorResponse)
    }
}
