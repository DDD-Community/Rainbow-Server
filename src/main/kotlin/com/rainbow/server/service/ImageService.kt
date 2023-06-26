package com.rainbow.server.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.amazonaws.util.IOUtils
import com.rainbow.server.domain.image.Image
import com.rainbow.server.domain.image.repository.ImageRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.IOException
import java.util.*

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val s3Client: AmazonS3Client
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Value("\${cloud.aws.s3.dir}")
    lateinit var dir: String

    @Transactional
    fun save(file: MultipartFile): Image {
        val originalFileName = file.originalFilename
        val saveFileName = genSaveFileName(originalFileName)

        // TODO: S3 경로로 변경
//        val filePath: String = System.getProperty("user.dir")!! // 임시 경로(추후 S3 경로로 변경)
//        val uploadDir = File(filePath).parent + "/images/"
//        Files.createDirectories(Paths.get(uploadDir))
//        uploadImage(uploadDir, file, saveFileName)
        upload(file, saveFileName)

        return imageRepository.save(Image(
            originalFileName = originalFileName,
            saveFileName = saveFileName))
    }

    private fun genSaveFileName(originalFilename: String?): String {
        val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
        val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)

        return UUID.randomUUID().toString() + "." + ext
    }

    @Throws(IOException::class)
    private fun upload(file: MultipartFile, saveFileName: String): String {
        val objMeta = ObjectMetadata()
        val bytes = IOUtils.toByteArray(file.inputStream)
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        s3Client.putObject(PutObjectRequest(bucket, dir + saveFileName, byteArrayIs, objMeta)
            .withCannedAcl(CannedAccessControlList.PublicRead))

        return s3Client.getUrl(bucket, dir + saveFileName).toString()
    }
}
