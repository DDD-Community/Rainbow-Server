package com.rainbow.server.config.cors

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
//            .allowedOriginPatterns("*") // 모든 도메인에 대해 액세스 허용
//            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
//            .allowedHeaders("localhost:3000") // 모든 헤더 허용
//            .allowCredentials(true) // 자격 증명 허용
//            .maxAge(3600) // CORS preflight 요청 결과 캐싱 시간 (초 단위)
//    }
        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
            .allowedOriginPatterns("*") // 모든 도메인에 대해 액세스 허용
            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
            .allowedHeaders() // 모든 헤더 허용
            .allowCredentials(true) // 자격 증명 허용
            .maxAge(3600) // CORS preflight 요청 결과 캐싱 시간 (초 단위)
    }
//        registry.addMapping("/**") // 모든 경로에 대해 CORS 설정 적용
//            .allowedOrigins("http://localhost:3000", "http://www.buybye.kr") // 허용할 도메인 목록
//            .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // 허용할 HTTP 메서드
//            .allowCredentials(true) // 자격 증명 허용
//            .maxAge(3600) // CORS preflight 요청 결과 캐싱 시간 (초 단위)
//    }
}
