package com.rainbow.server

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.DeleteObjectRequest
import com.rainbow.server.domain.image.repository.ImageRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.mock.web.MockMultipartFile
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class ImageUploadTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var imageRepository: ImageRepository

    @Autowired
    private lateinit var s3Client: AmazonS3Client

    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    @Test
    fun testImageUpload() {
        val file1 = MockMultipartFile("files", "image1.jpg", "image/jpeg", "test image1".toByteArray())
        val file2 = MockMultipartFile("files", "image2.jpg", "image/jpeg", "test image2".toByteArray())

        mockMvc.perform(
            multipart("/images/upload")
                .file(file1)
                .file(file2)
        )
            .andExpect(status().isOk())
    }

    @AfterEach
    fun cleanup() {
        val objectName1 = "image1.jpg"
        val objectName2 = "image2.jpg"

        s3Client.deleteObject(DeleteObjectRequest(bucket, objectName1))
        s3Client.deleteObject(DeleteObjectRequest(bucket, objectName2))

        val image1 = imageRepository.findByOriginalFileName("image1.jpg")
        val image2 = imageRepository.findByOriginalFileName("image2.jpg")

        image1?.let { imageRepository.delete(it) }
        image2?.let { imageRepository.delete(it) }
    }
}
