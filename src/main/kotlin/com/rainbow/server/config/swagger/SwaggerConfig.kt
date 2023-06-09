package com.rainbow.server.config.swagger
import org.springframework.context.annotation.Bean
import org.slf4j.LoggerFactory
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.ResponseEntity
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.service.ApiInfo
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.service.Contact
import springfox.documentation.swagger2.annotations.EnableSwagger2
import springfox.documentation.spi.DocumentationType
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebSession
import springfox.documentation.builders.RequestHandlerSelectors
import java.util.*

@Configuration
@EnableSwagger2
class SwaggerConfig (private val buildProperties: BuildProperties
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun api(): Docket{
        logger.debug("Starting Swagger...")

        return Docket(DocumentationType.SWAGGER_2)
            .enable(true)
            .useDefaultResponseMessages(false)
            .ignoredParameterTypes(
                WebSession::class.java,
                ServerHttpRequest::class.java,
                ServerHttpResponse::class.java,
                ServerWebExchange::class.java
            )
            .genericModelSubstitutes(
                Optional::class.java,
                ResponseEntity::class.java
            )
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.rainbow.server.rest"))
            .build().apiInfo(apiEndPointsInfo())

    }

    private fun apiEndPointsInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title(buildProperties.name)
            .description("무지개 API swagger doc")
            .contact(Contact("ChoJiWon", "https://github.com/Jiwon-cho", "gwon188@gmail.com"))
            .version(buildProperties.version)
            .build()
    }

}