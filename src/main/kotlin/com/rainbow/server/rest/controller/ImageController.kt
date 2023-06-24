package com.rainbow.server.rest.controller

import com.rainbow.server.service.ImageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    @PostMapping("/upload")
    fun postImage(@RequestParam(required=true) file: MultipartFile): ResponseEntity<Any> {
        val save = imageService.save(file)
        return ResponseEntity.ok().body(save)
    }
}