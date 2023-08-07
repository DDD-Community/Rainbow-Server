package com.rainbow.server.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.ApiKey
import springfox.documentation.service.AuthorizationScope
import springfox.documentation.service.Contact
import springfox.documentation.service.SecurityReference
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {
    private fun apiKey(): ApiKey = ApiKey(
        "Authorization",
        "Authorization",
        "header"
    )

    private fun defaultAuth() = listOf(
        SecurityReference(
            "Authorization",
            arrayOf(
                AuthorizationScope("global", "accessEverything")
            ),
        )
    )

    private fun securityContext() = SecurityContext.builder()
        .securityReferences(defaultAuth())
        .build()

    @Bean
    fun swagger(): Docket = Docket(DocumentationType.OAS_30)
        .useDefaultResponseMessages(false)
        .securityContexts(listOf(securityContext()))
        .securitySchemes(listOf(apiKey()))
        .forCodeGeneration(true)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.rainbow.server.rest"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .enable(true)

    private fun apiInfo(): ApiInfo = ApiInfoBuilder()
        .title("RainBow Server Api")
        .description("무지개 API swagger doc")
        .contact(Contact("ChoJiWon", "https://github.com/Jiwon-cho", "gwon188@gmail.com"))
        .build()
}
