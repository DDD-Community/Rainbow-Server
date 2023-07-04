package com.rainbow.server.rest.controller

import com.rainbow.server.rest.dto.member.MemberRequestDto
import com.rainbow.server.service.KakaoLoginService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/auth")
class AuthController(private val kakaoLoginService: KakaoLoginService,
                     @Value("\${oauth.kakao.client-id}")
                     private val clientId: String) {

    @GetMapping("/kakao")
    fun loginKakao(@RequestParam("code") code:String,response:HttpServletResponse): ResponseEntity<Any> {

        val info=kakaoLoginService.kaKaoLogin(code)
        val body= info.sessionKey
        if(body!=null){
            val cookie = Cookie("sessionKey", info.sessionKey  )
            cookie.path = "/" // 쿠키 경로 설정 (선택 사항)
            cookie.maxAge = 60*60*24*90
            response.addCookie(cookie)
            return ResponseEntity.ok().body(info)
        }

        return ResponseEntity.ok().body(info)
    }

    @PostMapping("/signIn")
    fun signIn(@RequestBody memberInfo:MemberRequestDto,response:HttpServletResponse):ResponseEntity<Any>{
        val newMember=kakaoLoginService.singIn(memberInfo)
        return ResponseEntity.ok().body(newMember)
    }



    @PostMapping("/logout")
    fun logout(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Any> {
        val sessionKey = getSessionKeyFromCookie(request)

        // Redis에서 세션 정보 삭제
        sessionKey?.let { kakaoLoginService.logout(it) }

        // 쿠키 삭제
        val cookie = Cookie("sessionKey", "")
        cookie.maxAge = 0
        cookie.path = "/"
        response.addCookie(cookie)

        return ResponseEntity.ok().build()
    }

    private fun getSessionKeyFromCookie(request: HttpServletRequest): String? {
        val cookies = request.cookies
        if (cookies != null) {
            for (cookie in cookies) {
                if (cookie.name == "sessionKey") {
                    return cookie.value
                }
            }
        }
        return null
    }

//    @GetMapping("/kakao/logout")
//    fun logoutKakao(code: String): ResponseEntity<KakaoUserLogout> {
//        return ResponseEntity.ok(kakaoLoginService.logout(code))
//    }

    @GetMapping("/kakao/signin")
    fun kakaoBackendSignPage(
    ): ResponseEntity<*> {
        val redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=http://localhost:8080/auth/kakao&response_type=code"
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