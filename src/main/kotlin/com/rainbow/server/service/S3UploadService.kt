//package com.rainbow.server.service
//
//import com.rainbow.server.config.s3.AwsConfig
//import com.amazonaws.util.IOUtils
//import org.springframework.stereotype.Service
//import org.springframework.web.multipart.MultipartFile
//import java.io.IOException
//import java.util.UUID
//
//@Service
//class S3UploadService(private val awsS3Config: AwsConfig) {
//    @Throws(IOException::class)
//    fun upload(file: MultipartFile): String {
//        val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
//        val objMeta = objectMetadata()
//        val bytes = IOUtils.toByteArray(file.inputStream)
//        objMeta.contentLength = bytes.size.toLong()
//
//        val byteArrayIs = ByteArrayInputStream(bytes)
//
//        awsS3Config.amazonSQSAsync()
//    }
//}
