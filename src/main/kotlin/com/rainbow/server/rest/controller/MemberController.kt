package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.goal.TotalSavedCost
import com.rainbow.server.rest.dto.member.*
import com.rainbow.server.service.GoalService
import com.rainbow.server.service.MemberService
import com.rainbow.server.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val goalService: GoalService,
    @Value("\${oauth.kakao.client-id}")
    private val clientId: String
) {

    val log = logger()

    @GetMapping("/login")
    fun kakaoLogin(code: String): CommonResponse<Any> {
        log.info(code)
        return success(memberService.login(code))
    }

    @GetMapping
    fun checkEmail(@RequestParam("email") email: String): CommonResponse<CheckDuplicateResponse> = success(memberService.checkEmail(email))

    @GetMapping
    fun checkNickname(@RequestParam("nickname") nickname: String): CommonResponse<CheckDuplicateResponse> = success(memberService.checkNickName(nickname))

    @GetMapping("/me")
    fun getCurrentLoginMember(): CommonResponse<MemberResponseDto> {
        return success(memberService.getCurrentMemberInfo())
    }

    @GetMapping("/me/saved-cost")
    fun getSavedCost(): CommonResponse<TotalSavedCost> {
        return success(goalService.getSavedCost())
    }

    @GetMapping("/me/goals")
    fun getGoals(@RequestParam month: String): CommonResponse<List<Any>> {
        return success(goalService.getYearlyGoals())
    }

    @GetMapping("/salary")
    fun getSalary(): CommonResponse<List<SalaryDto>> {
        return success(memberService.getSalaryRange())
    }

    @GetMapping("/salary/{id}")
    fun getMySalary(@PathVariable id:Long){
        memberService.getMySalary(id)
    }

//    @GetMapping("/suggestedMemberList")
//    fun getSuggestedMemberList(){
//        success(memberService.getSuggestedMemberList())
//    }

    @PostMapping("/signUp")
    fun signIn(@RequestBody memberInfo: MemberRequestDto, response: HttpServletResponse): CommonResponse<Any> {
        return success(memberService.singUp(memberInfo))
    }

    @PostMapping("/accessToken")
    fun login(@RequestBody request: JwtDto): CommonResponse<JwtDto> {
        return success(memberService.generateAccessToken(request))
    }

    @PostMapping("/logout")
    fun logout(): CommonResponse<Boolean> = success(memberService.logout())

    @GetMapping("/kakao/signin")
    fun kakaoBackendSignPage(): ResponseEntity<*> {
        val redirectUrl =
            "https://kauth.kakao.com/oauth/authorize?client_id=$clientId&redirect_uri=http://localhost:8080/member/login&response_type=code"
        val uri = URI(redirectUrl)
        val headers = HttpHeaders()
        headers.location = uri
        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER)
    }
}
