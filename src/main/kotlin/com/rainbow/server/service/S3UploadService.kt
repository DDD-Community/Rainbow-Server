package com.rainbow.server.service

import com.rainbow.server.config.s3.AwsConfig
import org.springframework.stereotype.Service

@Service
class S3UploadService(private val awsS3Config: AwsConfig)