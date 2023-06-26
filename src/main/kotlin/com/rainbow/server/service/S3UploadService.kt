package com.rainbow.server.service

//import com.amazonaws.services.s3.AmazonS3Client
//import com.rainbow.server.config.s3.AwsConfig
//import com.amazonaws.util.IOUtils
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Service
//import org.springframework.web.multipart.MultipartFile
//import java.io.IOException
//import java.util.UUID

//@Service
//class S3Service(
//    private val s3Client: AmazonS3Client
//) {
//
//    @Value("\${cloud.aws.s3.bucket}")
//    lateinit var bucket: String
//
//    @Value("\${cloud.aws.s3.dir}")
//    lateinit var dir: String
//
//    @Throws(IOException::class)
//    fun upload(file: MultipartFile): String {
//        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
//
//    }
//}
