package com.rainbow.server

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.PutObjectRequest
import com.rainbow.server.domain.image.Image
import com.rainbow.server.domain.image.repository.ImageRepository
import com.rainbow.server.rest.controller.ImageController
import com.rainbow.server.service.ImageService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import java.net.URI

@ExtendWith(MockitoExtension::class, SpringExtension::class)
@WebMvcTest(ImageController::class)
@AutoConfigureMockMvc
class ImageUploadServiceTests {
    // TODO: Autowired 해결
    @Autowired
    private lateinit var imageService: ImageService

    @MockBean
    private lateinit var imageRepository: ImageRepository

    @MockBean
    private lateinit var s3Client: AmazonS3Client

    @Test
    @DisplayName("Image Upload")
    fun testSaveAll() {
        val files = mutableListOf(MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".toByteArray()))
        val saveFileName = "abc123.jpg"
        val imageUrl = "https://rainbow-github-actions-s3-bucket.s3.ap-northeast-2.amazonaws.com/image/abc123.jpg"
        val inputStream = file.inputStream

        `when`(imageRepository.save(any(Image::class.java))).thenReturn(Image(id=1, originalFileName = "test.jpg", saveFileName = saveFileName))
        `when`(s3Client.putObject(any(PutObjectRequest::class.java))).thenReturn(null)
        `when`(s3Client.getUrl(any(String::class.java), any(String::class.java))).thenReturn(URI(imageUrl).toURL())

        imageService.saveAll(file)

        verify(imageRepository).save(any(Image::class.java))
        verify(s3Client).putObject(any(PutObjectRequest::class.java))
        verify(s3Client).getUrl(any(String::class.java), any(String::class.java))
    }
}