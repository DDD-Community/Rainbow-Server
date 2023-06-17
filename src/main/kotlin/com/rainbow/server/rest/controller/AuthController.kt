package com.rainbow.server.rest.controller

import com.rainbow.server.auth.KakaoUserLogout
import com.rainbow.server.service.KakaoLoginService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import org.springframework.http.HttpHeaders

@RestController
@RequestMapping("/auth")
class AuthController(private val kakaoLoginService: KakaoLoginService,
                     @Value("\${oauth.kakao.client-id}")
                     private val clientId: String) {

    @GetMapping("/kakao")
    fun loginKakao(@RequestParam("code") code:String): ResponseEntity<Any> {
        return ResponseEntity.ok(kakaoLoginService.login(code))
    }

    @GetMapping("/kakao/logout")
    fun logoutKakao(code: String): ResponseEntity<KakaoUserLogout> {
        return ResponseEntity.ok(kakaoLoginService.logout(code))
    }

    @GetMapping("/kakao/signin")
    fun kakaoBackendSignPage(
    ): ResponseEntity<*> {
        val redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=http://43.201.219.27:8080/auth/kakao&response_type=code"
        val uri = URI(redirectUrl)
        val headers = HttpHeaders()
        headers.location = uri
        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER)
    }


    @GetMapping("/get")
    fun findById(code: String): ResponseEntity<Any>{
        return ResponseEntity.ok(kakaoLoginService.getById(code))
    }
}
