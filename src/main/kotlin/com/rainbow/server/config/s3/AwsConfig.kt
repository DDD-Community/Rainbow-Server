package com.rainbow.server.config.s3

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig {

    @Value("\${cloud.aws.credentials.access-key}")
    private lateinit var awsAccessKey: String

    @Value("\${cloud.aws.credentials.secret-key}")
    private lateinit var awsSecretKey: String

    @Value("\${cloud.aws.region.static}")
    private lateinit var regions: Regions

    @Bean(name = ["awsCredentials"])
    fun awsCredentials(): AWSCredentials {
        return BasicAWSCredentials(awsAccessKey, awsSecretKey)
    }

    @Bean
    fun amazonS3ClientSeoul(@Qualifier("awsCredentials") awsCredentials: AWSCredentials): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .withRegion(regions)
            .build()
    }
}
