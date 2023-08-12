package com.rainbow.server.service

import com.rainbow.server.auth.KakaoApiClient
import com.rainbow.server.auth.KakaoInfoResponse
import com.rainbow.server.auth.jwt.JwtProvider
import com.rainbow.server.auth.security.getCurrentLoginUserId
import com.rainbow.server.config.redis.RefreshToken
import com.rainbow.server.config.redis.RefreshTokenRepository
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.domain.member.repository.SalaryRepository
import com.rainbow.server.rest.dto.member.CheckDuplicateResponse
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
    private val jwtProvider: JwtProvider,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val salaryRepository: SalaryRepository,
) {

    fun getCurrentLoginMember(): Member = memberRepository.findById(getCurrentLoginUserId()).orElseThrow()

    fun getCurrentMemberInfo() = MemberResponseDto(getCurrentLoginMember())

//    @Transactional
//    fun kaKaoLogin(code:String):MemberResponseDto {
//        val accessToken = client.requestAccessToken(code)
//        val infoResponse = client.requestOauthInfo(accessToken)
//        val member=findMember(infoResponse)
// //        if(member!=null) return MemberResponseDto(sessionService.storeSessionId(member).getSessionKey(),member)
//
//        return MemberResponseDto(infoResponse.email)
//
//    }

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

        return MemberResponseDto(nickName = infoResponse.kakaoProfile.nickname, kakaoId = username, email = infoResponse.email, birthDate = null, salary = 0, gender = infoResponse.kakaoAccount.gender)
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
        val refreshToken = refreshTokenRepository.findByRefreshToken(request.refreshToken) ?: throw NullPointerException("refreshToken 없음")
        val member = memberRepository.findById(refreshToken.memberId).orElseThrow()
        return JwtDto(jwtProvider.generateToken(member), refreshToken.refreshToken)
    }

//    fun getSuggestedMemberList() {
//        val member = getCurrentLoginMember()
//        val memberSet: MutableSet<Member> = mutableSetOf()
//        val allSimilarMembers = memberRepository.findSuggestedMemberList(member)
//        // memberRepository.findMemberListBySalary(member.salaryStart, member.salaryEnd)
//        val salaryMemberList = mutableListOf<Member>()
//        val birthDateList = mutableListOf<Member>()
//
//        val newBies = memberRepository.findNewbies()
// //        if(newBies.isNotEmpty()){
// //            newBies.stream().map { m->memberSet.add(m) }
// //        }
// //        if(allSimilarMembers.isNotEmpty()){
// //           allSimilarMembers.stream().map { m->{
// //               if(m.salaryStart==member.salaryStart) salaryMemberList.add(m)
// //               if(m.birthDate == member.birthDate) birthDateList.add(m)
// //           } }
// //        }
// //        if(salaryMemberList.isNotEmpty()){
// //            val nowSize=memberSet.size
// //            while(true){
// //                val setSize=memberSet.size
// //
// //                if((setSize-nowSize)>5) break
// //            }
// //        }
//    }

//    fun getRandomFriends(): HashSet<MemberResponseDto> {
//        val member = getCurrentLoginMember()
//        val allSimilarMembers = memberRepository.findSuggestedMemberList(member)
//        val arr = makeRandomNumbs(allSimilarMembers.size)
//        val friendsList = mutableListOf<MemberResponseDto>()
//        val friendsSet = HashSet<MemberResponseDto>()
//        val newBies = memberRepository.findNewbies()
// //        arr.forEach { n->friendsList.add(MemberResponseDto(allSimilarMembers[n])) }
//        arr.forEach { n -> friendsSet.add(MemberResponseDto(allSimilarMembers[n])) }
//        newBies.forEach { m -> friendsSet.add(MemberResponseDto(m)) }
//
//        return friendsSet
//    }

//    private fun makeRandomNumbs(n: Int): IntArray {
//        val randNum = IntArray(15)
//        // 구하고자하는 랜덤번호 3가지를 넣을 정수 배열을 선언한다.
//
//        val switch = BooleanArray(n)
//        // switch는 10개의 공간이 모두 0으로 채워진 배열이라고 가정한다.
//
//        for (i in switch.indices) {
//            switch[i] = false
//            // 스위치가 false라는 말은 값이 배열에 있는 값이 전부 0 으로 세팅 되었다는 말이다.
//        }
//
//        var w = 0
//        while (w < 5) {
//            // 뽑고 싶은 랜덤 번호의 숫자가 3개이므로 w은 <3으로 설정한다.
//
//            val r = (Math.random() * n).toInt()
//            // var r은 1~9 사이에서 랜덤으로 뽑힌 숫자이다.
//
//            if (!switch[r]) {
//                switch[r] = true
//                // switch[r] 번째는 초기에 false, 즉 0으로 세팅되었으므로 if문의 조건문으로 성립된다.
//                // 조건이 성립된후, r번째에 있는 switch 배열의 값은 true로 변한다.
//
//                randNum[w] = r + 1
//                // r은 1에서 9까지의 값이므로 1을 더한다. 그리하여 최대값을 10으로 만든다.
//                // 제일 처음 선언헌 랜덤번호 배열에 r+1의 값을 대입한다.
//
//                w++
//            }
//        }
//        for (i in randNum.indices) {
//            println("randNum[$i] = ${randNum[i]}")
//        }
//
//        return randNum
//    }

    private fun findMember(infoResponse: KakaoInfoResponse): Member? {
        return memberRepository.findByEmail(infoResponse.email)
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

    fun checkEmail(email: String): CheckDuplicateResponse = CheckDuplicateResponse(memberRepository.existsByEmail(email))

    fun checkNickName(nickName: String): CheckDuplicateResponse = CheckDuplicateResponse(
        memberRepository.existsByNickName(nickName),
    )

    fun getSalaryRange(): List<SalaryDto> {
        return salaryRepository.findAll().stream().map { s -> SalaryDto(s) }.toList().sortedBy { it.idx }
    }

    fun getMySalary(id: Long) {
        salaryRepository.findById(id)
    }
}
