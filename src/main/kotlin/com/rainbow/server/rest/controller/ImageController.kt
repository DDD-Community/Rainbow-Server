// package com.rainbow.server.rest.controller
//
// import com.rainbow.server.service.ImageService
// import org.springframework.http.ResponseEntity
// import org.springframework.web.bind.annotation.PathVariable
// import org.springframework.web.bind.annotation.PostMapping
// import org.springframework.web.bind.annotation.RequestMapping
// import org.springframework.web.bind.annotation.RequestParam
// import org.springframework.web.bind.annotation.RestController
// import org.springframework.web.multipart.MultipartFile
// import java.util.*
//
// @RestController
// @RequestMapping("/images")
// class ImageController(private val imageService: ImageService) {
//
//    @PostMapping("/upload/{expense_id}")
//    fun postImage(@RequestParam(required=true) files: List<MultipartFile>, @PathVariable(name = "expense_id") expenseId: Long): ResponseEntity<Any> {
//        val save = imageService.saveAll(files, expenseId)
//        return ResponseEntity.ok().body(save)
//    }
// }
