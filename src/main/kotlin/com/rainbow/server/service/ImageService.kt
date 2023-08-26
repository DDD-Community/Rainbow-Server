package com.rainbow.server.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.repository.ExpenseRepository
import com.rainbow.server.domain.image.entity.Image
import com.rainbow.server.domain.image.repository.ImageRepository
import com.rainbow.server.exception.CustomException
import com.rainbow.server.exception.ErrorCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.IOException
import java.util.UUID
import kotlin.collections.mutableListOf

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val expenseRepository: ExpenseRepository,
    private val s3Client: AmazonS3Client,
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Transactional
    fun saveAll(files: List<MultipartFile>, expenseId: Long): MutableList<Image> {
        val expense = expenseRepository.findById(expenseId).orElseThrow { CustomException(ErrorCode.ENTITY_NOT_FOUND, "expense") }
        val images = addImages(files, expense)
        return imageRepository.saveAll(images)
    }

    private fun addImages(files: List<MultipartFile>?, expense: Expense): MutableList<Image> {
        val images = mutableListOf<Image>()

        if (files.size > 2) {
            throw CustomException(ErrorCode.FILE_LIMIT_EXCEEDED)
        } else if (files.isEmpty()) {
            return images
        }

        for (file in files) {
            val originalFileName = file?.originalFilename
            val saveFileName = generateSaveFileName(originalFileName)

            try {
                upload(file!!, saveFileName)
            } catch (e: IOException) {
                throw CustomException(ErrorCode.INVALID_INPUT_FILE)
            }

            images.add(
                Image(
                    originalFileName = originalFileName,
                    saveFileName = saveFileName,
                    expense = expense,
                ),
            )
        }

        return images
    }

    private fun generateSaveFileName(originalFilename: String?): String {
        val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
        val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)

        return UUID.randomUUID().toString() + "." + ext
    }

    @Throws(IOException::class)
    private fun upload(file: MultipartFile, saveFileName: String): String {
        val objMeta = ObjectMetadata()
        val bytes = file.inputStream.use { it.readBytes() }
        objMeta.contentLength = bytes.size.toLong()

        val byteArrayIs = ByteArrayInputStream(bytes)

        s3Client.putObject(PutObjectRequest(bucket, saveFileName, byteArrayIs, objMeta).withCannedAcl(CannedAccessControlList.PublicRead))

        file.inputStream.close()

        return s3Client.getUrl(bucket, saveFileName).toString()
    }
}
