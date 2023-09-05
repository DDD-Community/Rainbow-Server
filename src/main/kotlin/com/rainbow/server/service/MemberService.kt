package com.rainbow.server.service

import com.rainbow.server.auth.KakaoApiClient
import com.rainbow.server.auth.KakaoInfoResponse
import com.rainbow.server.auth.jwt.JwtProvider
import com.rainbow.server.auth.security.getCurrentLoginUserId
import com.rainbow.server.config.redis.RefreshToken
import com.rainbow.server.config.redis.RefreshTokenRepository
import com.rainbow.server.domain.expense.repository.ExpenseRepository
import com.rainbow.server.domain.member.entity.Follow
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.FollowRepository
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.domain.member.repository.SalaryRepository
import com.rainbow.server.rest.dto.expense.ExpenseResponse
import com.rainbow.server.rest.dto.expense.FriendsExpenseDto
import com.rainbow.server.rest.dto.member.CheckDuplicateResponse
import com.rainbow.server.rest.dto.member.ConditionFilteredMembers
import com.rainbow.server.rest.dto.member.FollowingRequest
import com.rainbow.server.rest.dto.member.FriendDetailResponse
import com.rainbow.server.rest.dto.member.FriendSearchResponse
import com.rainbow.server.rest.dto.member.JwtDto
import com.rainbow.server.rest.dto.member.MemberRequestDto
import com.rainbow.server.rest.dto.member.MemberResponseDto
import com.rainbow.server.rest.dto.member.SalaryDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.NullPointerException
import java.util.UUID
import kotlin.streams.toList

@Service
@Transactional(readOnly = true)
class MemberService(
    private val client: KakaoApiClient,
    private val memberRepository: MemberRepository,
    private val expenseRepository: ExpenseRepository,
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val salaryRepository: SalaryRepository,
    private val followRepository: FollowRepository,
) {

    fun getCurrentLoginMember(): Member = memberRepository.findById(getCurrentLoginUserId()).orElseThrow()

    fun getCurrentMemberInfo() = MemberResponseDto(getCurrentLoginMember())

    @Transactional
    fun login(authorizedCode: String): Any {
        val accessToken = client.requestAccessToken(authorizedCode)
        val infoResponse = client.requestOauthInfo(accessToken)
        println(infoResponse.id)

        val member = findMember(infoResponse)

        /* username, password 는 아래 UsernamePasswordAuthenticationToken 의 형식을 맞춰주기 위해
        이름을 변경한 것 */
//        val username = infoResponse.kakaoAccount.profile
        val username = infoResponse.id

        // password 는 서비스에서 사용X, Security 설정을 위해 넣어준 값
        val password = username.toString()

        if (member != null) {
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(username, password)

            val refreshToken = RefreshToken(UUID.randomUUID().toString(), member.memberId)
            refreshTokenRepository.save(refreshToken)

            // JWT 발급
            return JwtDto(jwtProvider.generateToken(member), refreshToken.refreshToken)
        }

        return MemberResponseDto(
            nickName = infoResponse.kakaoProfile.nickname,
            kaKaoId = username,
            email = infoResponse.email,
            birthDate = null,
            salary = "0",
            gender = infoResponse.kakaoAccount.gender,
        )
    }

    @Transactional
    fun singUp(member: MemberRequestDto): JwtDto {
        val newMember = newMember(member)

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(newMember.kaKaoId, newMember.password)
        val refreshToken = RefreshToken(UUID.randomUUID().toString(), newMember.memberId)
        refreshTokenRepository.save(refreshToken)

        // JWT 발급
        return JwtDto(jwtProvider.generateToken(newMember), refreshToken.refreshToken)
    }

    fun generateAccessToken(request: JwtDto): JwtDto {
        val refreshToken = refreshTokenRepository.findByRefreshToken(request.refreshToken)
            ?: throw NullPointerException("refreshToken 없음")
        val member = memberRepository.findById(refreshToken.memberId).orElseThrow()
        return JwtDto(jwtProvider.generateToken(member), refreshToken.refreshToken)
    }

    fun getFriendRecommendations(): List<ConditionFilteredMembers> {
        val currentMember = getCurrentLoginMember() // 현재 로그인한 멤버를 가져오는 로직

        val maxRecommendedPerCategory = 4

        val targetTotalCount = maxRecommendedPerCategory * 4

        val recommendedMembers = mutableSetOf<Member>()

        val filteredRecommendedMembers = mutableListOf<ConditionFilteredMembers>()

        val agePredicate = memberRepository.getAgePredicate(currentMember.birthDate)
        val similarAgeMembers =
            memberRepository.fetchMembersForCondition(agePredicate, recommendedMembers, maxRecommendedPerCategory, currentMember)
        recommendedMembers.addAll(similarAgeMembers)
        filteredRecommendedMembers.add(ConditionFilteredMembers("age", similarAgeMembers.map { MemberResponseDto(it) }))

        val salaryPredicate = memberRepository.getSalaryPredicate(currentMember.salary)
        val similarSalaryMembers =
            memberRepository.fetchMembersForCondition(salaryPredicate, recommendedMembers, maxRecommendedPerCategory, currentMember)
        recommendedMembers.addAll(similarSalaryMembers)
        filteredRecommendedMembers.add(ConditionFilteredMembers("salary", similarSalaryMembers.map { MemberResponseDto(it) }))

        val topExpenseCreators = memberRepository.topExpenseCreators(recommendedMembers, currentMember, maxRecommendedPerCategory)
        recommendedMembers.addAll(topExpenseCreators)
        filteredRecommendedMembers.add(ConditionFilteredMembers("expense", topExpenseCreators.map { MemberResponseDto(it) }))

        val remainingCount = targetTotalCount - recommendedMembers.size
        val recentJoinMembers = memberRepository.fetchRecentJoinMembers(recommendedMembers, remainingCount, currentMember)
        filteredRecommendedMembers.add(ConditionFilteredMembers("recent", recentJoinMembers.map { MemberResponseDto(it) }))

        return filteredRecommendedMembers
    }

    private fun findMember(infoResponse: KakaoInfoResponse): Member? {
        return memberRepository.findByEmail(infoResponse.email)
    }

    fun findByNickName(nickName: String): List<FriendSearchResponse>? {
        return memberRepository.findAllByNickName(nickName)?.stream()
            ?.map { m -> FriendSearchResponse(m, isFriendOrNot(m.memberId)) }?.toList()
    }

    private fun newMember(member: MemberRequestDto): Member {
        val newMember = Member(
            email = member.email,
            password = passwordEncoder.encode(member.email),
            nickName = member.nickName,
            birthDate = member.birthDate,
            salary = member.salary,
            gender = member.gender,
            kaKaoId = member.kaKaoId,
        )

        return memberRepository.save(newMember)
    }

    fun logout(): Boolean {
        return client.logout(getCurrentLoginMember().kaKaoId)
    }

    fun checkEmail(email: String): CheckDuplicateResponse =
        CheckDuplicateResponse(memberRepository.existsByEmail(email))

    fun checkNickName(nickName: String): CheckDuplicateResponse = CheckDuplicateResponse(
        memberRepository.existsByNickName(nickName),
    )

    fun getSalaryRange(): List<SalaryDto> {
        return salaryRepository.findAll().stream().map { s -> SalaryDto(s) }.toList().sortedBy { it.idx }
    }

    @Transactional
    fun followMember(followingId: FollowingRequest) {
        val follow = Follow(fromMember = getCurrentLoginUserId(), toMember = followingId.followingId)
        followRepository.save(follow)
    }
    @Transactional
    fun followMembers(followIds:List<FollowingRequest>){
        followIds.stream().forEach { i->followRepository.save(Follow(fromMember = getCurrentLoginUserId(), toMember = i.followingId)) }
    }

    fun getAllFollowingNames(): List<String?> {
        return followRepository.finAllByFromMemberWithMember(getCurrentLoginUserId())
    }

    fun isFriendOrNot(checkId: Long): Boolean {
        return followRepository.existsByFromMemberAndToMember(getCurrentLoginUserId(), checkId)
    }

    fun getAnotherMemberInfo(memberId: Long, page: Long?): FriendDetailResponse {
        val pageSize = 10L
        val pageNum = ((page ?: 1L) - 1L) * pageSize
        val anotherMember = memberRepository.findById(memberId).orElseThrow()
        val goal = anotherMember.goalList.maxByOrNull { it.time }
        val expenseList = expenseRepository.getAnotherMemberExpenseList(anotherMember.memberId, pageSize, pageNum)
        val expenseResponseList = expenseList?.stream()?.map { e -> ExpenseResponse(e) }?.toList()
        val isFriend = isFriendOrNot(anotherMember.memberId)

        val groupedExpenses = expenseResponseList?.groupBy { it?.date }
            ?.toSortedMap(compareByDescending { it })

        return FriendDetailResponse(anotherMember, groupedExpenses, isFriend, goal)
    }

    fun getFriendsFeeds(lastId: Long?): List<FriendsExpenseDto>? {
        val followingList = followRepository.findAllByFromMember(getCurrentLoginUserId())
        val followingMembers = followingList.mapNotNull { f -> memberRepository.findById(f.toMember).orElse(null) }
        return expenseRepository.getFriendsFeedList(lastId, getCurrentLoginMember(), followingMembers)
    }
}
