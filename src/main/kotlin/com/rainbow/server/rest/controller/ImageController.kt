package com.rainbow.server.rest.controller

import com.rainbow.server.service.ImageService
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@Slf4j
@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    val logger: Logger = LoggerFactory.getLogger(ImageController::class.java)

    @PostMapping("/upload/{expense_id}")
    fun postImage(
        @RequestParam(required = true)
        files: List<MultipartFile>,
        @PathVariable(name = "expense_id")
        expenseId: Long,
    ) {
        try {
            imageService.saveAll(files, expenseId)
        } catch (e: Exception) {
            logger.error(e.toString())
        }
    }
}
