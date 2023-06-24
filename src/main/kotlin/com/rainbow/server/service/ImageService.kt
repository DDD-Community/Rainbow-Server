package com.rainbow.server.service

import com.rainbow.server.domain.image.Image
import com.rainbow.server.domain.image.repository.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

@Service
class ImageService(
    private val imageRepository: ImageRepository
) {
    @Transactional
    fun save(file: MultipartFile): Image {
        val originalFileName = file.originalFilename
        val saveFileName = genSaveFileName(originalFileName)
        val filePath: String = System.getProperty("user.dir")!! // 임시 경로(추후 S3 경로로 변경)
        val uploadDir = File(filePath).parent + "/images/"
        Files.createDirectories(Paths.get(uploadDir))
        uploadImage(uploadDir, file, saveFileName)

        return imageRepository.save(Image(
            originalFileName = originalFileName,
            saveFileName = saveFileName))
    }

    private fun genSaveFileName(originalFilename: String?): String {
        val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
        val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)

        return UUID.randomUUID().toString() + "." + ext
    }

    private fun uploadImage(uploadDir: String, file: MultipartFile, saveFileName: String) {
        println("file path: $uploadDir")

        if (file != null && file.originalFilename != null) {
            file.transferTo(File(uploadDir + saveFileName))
        }
    }
}