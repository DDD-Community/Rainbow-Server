package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.goal.TotalHistory
import com.rainbow.server.rest.dto.goal.YearlyGoalData
import com.rainbow.server.rest.dto.member.CheckDuplicateResponse
import com.rainbow.server.rest.dto.member.ConditionFilteredMembers
import com.rainbow.server.rest.dto.member.FollowingRequest
import com.rainbow.server.rest.dto.member.FriendDetailResponse
import com.rainbow.server.rest.dto.member.FriendSearchResponse
import com.rainbow.server.rest.dto.member.JwtDto
import com.rainbow.server.rest.dto.member.MemberRequestDto
import com.rainbow.server.rest.dto.member.MemberResponseDto
import com.rainbow.server.rest.dto.member.SalaryDto
import com.rainbow.server.service.GoalService
import com.rainbow.server.service.MemberService
import com.rainbow.server.util.logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/members")
class MemberController(
    private val memberService: MemberService,
    private val goalService: GoalService,
    @Value("\${oauth.kakao.client-id}")
    private val clientId: String,
) {

    val log = logger()

    @GetMapping("/login")
    fun kakaoLogin(code: String): CommonResponse<Any> {
        log.info(code)
        return success(memberService.login(code))
    }

    @GetMapping("/email/check")
    fun checkEmail(@RequestParam("email") email: String): CommonResponse<CheckDuplicateResponse> = success(memberService.checkEmail(email))

    @GetMapping("/nickname/check")
    fun checkNickname(@RequestParam("nickname") nickname: String): CommonResponse<CheckDuplicateResponse> = success(memberService.checkNickName(nickname))

    @GetMapping("/me")
    fun getCurrentLoginMember(): CommonResponse<MemberResponseDto> {
        return success(memberService.getCurrentMemberInfo())
    }

    @GetMapping("/me/history")
    fun getSavedCost(): CommonResponse<TotalHistory> {
        return success(memberService.getHistory())
    }

    @GetMapping("/me/goals")
    fun getGoals(): CommonResponse<YearlyGoalData> {
        return success(goalService.getYearlyGoals())
    }

    @GetMapping("/salary")
    fun getSalary(): CommonResponse<List<SalaryDto>> {
        return success(memberService.getSalaryRange())
    }

    @GetMapping("/suggestedMemberList")
    fun getSuggestedMemberList(): CommonResponse<List<ConditionFilteredMembers>> {
        return success(memberService.getFriendRecommendations())
    }

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

    @DeleteMapping("/delete")
    fun delete(): CommonResponse<String> = success(memberService.delete())

    @GetMapping("/search")
    fun findByNickName(@RequestParam(name = "nickname")nickname: String): CommonResponse<List<FriendSearchResponse>?> {
        return success(memberService.findByNickName(nickname))
    }

    @GetMapping("/{memberId}")
    fun getFeedById(@PathVariable(name = "memberId")memberId: Long, @RequestParam(required = false, name = "page")page: Long?): CommonResponse<FriendDetailResponse> {
        return success(memberService.getAnotherMemberInfo(memberId, page))
    }

    @PostMapping("/following")
    fun followMember(@RequestBody followingId: FollowingRequest) {
        memberService.followMember(followingId)
    }

    @PostMapping("/first-following")
    fun followMembers(@RequestBody followingIds: List<FollowingRequest>) {
        memberService.followMembers(followingIds)
    }

    @GetMapping("/following/names")
    fun getAllFollowingNames(): CommonResponse<List<String?>> {
        return success(memberService.getAllFollowingNames())
    }

    @GetMapping("/following/check")
    fun checkFriend(id: Long): Boolean {
        return memberService.isFriendOrNot(id)
    }

    @GetMapping("/kakao/signin")
    fun kakaoBackendSignPage(): ResponseEntity<*> {
        val redirectUrl =
            "https://kauth.kakao.com/oauth/authorize?client_id=$clientId&redirect_uri=http://localhost:8080/member/login&response_type=code"
        val uri = URI(redirectUrl)
        val headers = HttpHeaders()
        headers.location = uri
        return ResponseEntity<Any>(headers, HttpStatus.SEE_OTHER)
    }

    @PatchMapping("/profile-image")
    fun uploadImage(
        @RequestParam(name = "file") file: MultipartFile,
    ): CommonResponse<MemberResponseDto> {
        return success(memberService.saveImage(file))
    }
}
