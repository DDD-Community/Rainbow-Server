package com.rainbow.server.rest.controller

import com.rainbow.server.service.ImageService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    @PostMapping("/upload/{expense_id}")
    fun postImage(
        @RequestParam(required = true)
        files: List<MultipartFile>,
        @PathVariable(name = "expense_id")
        expenseId: Long,
    ) {
        imageService.saveAll(files, expenseId)
    }
}
