package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.goal.TotalSavedCost
import com.rainbow.server.rest.dto.member.*
import com.rainbow.server.service.GoalService
import com.rainbow.server.service.MemberService
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
class MemberController(private val memberService: MemberService,
                       private val goalService: GoalService,
                       @Value("\${oauth.kakao.client-id}")
                     private val clientId: String) {

    val log = logger()

    @GetMapping("/login")
    fun kakaoLogin(code: String): CommonResponse<Any> {
        log.info(code)
        return success(memberService.login(code))
    }

    @PostMapping("/checkEmail")
    fun checkEmail(@RequestBody email:DuplicateCheck):Boolean{
        return memberService.checkEmail(email.data)
    }

    @PostMapping("/checkNickName")
    fun checkNickName(@RequestBody nickName: DuplicateCheck):Boolean{
        return memberService.checkNickName(nickName.data)
    }


    @GetMapping("/me")
    fun getCurrentLoginMember():CommonResponse<MemberResponseDto>{
        return success(memberService.getCurrentMemberInfo())
    }

    @GetMapping("/savedCost")
    fun getSavedCost():CommonResponse<TotalSavedCost>{
        return success(goalService.getSavedCost())
    }


    @GetMapping("/myGoals")
    fun getGoals(@RequestParam month:String):CommonResponse<List<Any>>{
        return success(goalService.getYearlyGoals())
    }

    @GetMapping("/salary")
    fun getSalary():CommonResponse<List<SalaryDto>>{
        return success(memberService.getSalaryRange())
    }


//    @GetMapping("/suggestedMemberList")
//    fun getSuggestedMemberList(){
//        success(memberService.getSuggestedMemberList())
//    }

    @PostMapping("/signUp")
    fun signIn(@RequestBody memberInfo:MemberRequestDto,response:HttpServletResponse):CommonResponse<Any>{
        return success(memberService.singUp(memberInfo))
    }

    @PostMapping("/accessToken")
    fun login(@RequestBody  request: JwtDto): CommonResponse<JwtDto> {
        return success(memberService.generateAccessToken(request))
    }

    @PostMapping("/logout")
    fun logout(): CommonResponse<Boolean> = success(memberService.logout())

    @GetMapping("/kakao/signin")
    fun kakaoBackendSignPage(
    ): ResponseEntity<*> {
        val redirectUrl = "https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=http://localhost:8080/member/login&response_type=code"
        val uri = URI(redirectUrl)
        val headers = HttpHeaders()
        headers.location = uri
        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER)
    }


    @GetMapping("/get")
    fun findById(code: String): ResponseEntity<Any>{
        return ResponseEntity.ok(memberService.getById(code))
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
}