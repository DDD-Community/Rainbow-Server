package com.rainbow.server.rest.controller

import com.rainbow.server.exception.CustomException
import com.rainbow.server.rest.dto.ErrorResponse
import com.rainbow.server.service.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    @PostMapping("/upload")
    fun postImage(@RequestParam(required=true) files: List<MultipartFile>): ResponseEntity<Any> {
        val save = imageService.saveAll(files)
        return ResponseEntity.ok().body(save)
    }
}

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(
            status = e.errorCode.status.value(),
            error = HttpStatus.BAD_REQUEST,
            message = e.errorCode.message
        )
        return ResponseEntity.status(e.errorCode.status).body(errorResponse)
    }

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    fun handleMaxUploadSizeExceededException(e: MaxUploadSizeExceededException): ResponseEntity<Any> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.PAYLOAD_TOO_LARGE.value(),
            error = HttpStatus.PAYLOAD_TOO_LARGE,
            message = "파일 업로드 용량 제한을 초과했습니다."
        )
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE.value()).body(errorResponse)
    }
}
