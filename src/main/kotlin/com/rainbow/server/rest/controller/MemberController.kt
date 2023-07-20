package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.member.JwtDto
import com.rainbow.server.rest.dto.member.MemberRequestDto
import com.rainbow.server.rest.dto.member.MemberResponseDto
import com.rainbow.server.service.GoalService
import com.rainbow.server.service.KakaoLoginService
import com.rainbow.server.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/member")
class MemberController(private val kakaoLoginService: KakaoLoginService,
                       private val goalService: GoalService,
                       @Value("\${oauth.kakao.client-id}")
                     private val clientId: String) {

    val log = logger()

    @GetMapping("/login")
    fun kakaoLogin(code: String): CommonResponse<Any> {
        log.info(code)
        return success(kakaoLoginService.login(code))
    }

    @GetMapping("/me")
    fun getCurrentLoginMember():CommonResponse<MemberResponseDto>{
        return success(kakaoLoginService.getCurrentMemberInfo())
    }

    @GetMapping("/myGoals")
    fun getGoals(@RequestParam month:String){

    }

//    @GetMapping("/kakao")
//    fun loginKakao(@RequestParam("code") code:String,response:HttpServletResponse): ResponseEntity<Any> {
//
//        val info=kakaoLoginService.kaKaoLogin(code)
//        val body= info.sessionKey
//        if(body!=null){
//            val cookie = Cookie("sessionKey", info.sessionKey  )
//            cookie.path = "/" // 쿠키 경로 설정 (선택 사항)
//            cookie.maxAge = 60*60*24*90
//            response.addCookie(cookie)
//            return ResponseEntity.ok().body(info)
//        }
//
//        return ResponseEntity.ok().body(info)
//    }

    @PostMapping("/signUp")
    fun signIn(@RequestBody memberInfo:MemberRequestDto,response:HttpServletResponse):CommonResponse<Any>{
        return success(kakaoLoginService.singIn(memberInfo))
    }

    @PostMapping("/accessToken")
    fun login(@RequestBody  request: JwtDto): CommonResponse<JwtDto> {
        return success(kakaoLoginService.generateAccessToken(request))
    }

    @PostMapping("/logout")
    fun logout(): CommonResponse<Boolean> = success(kakaoLoginService.logout())

//    fun logout(request: HttpServletRequest, response: HttpServletResponse): ResponseEntity<Any> {
//        val sessionKey = getSessionKeyFromCookie(request)
//
//        // Redis에서 세션 정보 삭제
//        sessionKey?.let { kakaoLoginService.logout(it) }
//
//        // 쿠키 삭제
//        val cookie = Cookie("sessionKey", "")
//        cookie.maxAge = 0
//        cookie.path = "/"
//        response.addCookie(cookie)
//
//        return ResponseEntity.ok().build()
//    }


//    @GetMapping("/kakao/logout")
//    fun logoutKakao(code: String): ResponseEntity<KakaoUserLogout> {
//        return ResponseEntity.ok(kakaoLoginService.logout(code))
//    }

    @GetMapping("/kakao/signin")
    fun kakaoBackendSignPage(
    ): ResponseEntity<*> {
        val redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=http://localhost:8080/auth/login&response_type=code"
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