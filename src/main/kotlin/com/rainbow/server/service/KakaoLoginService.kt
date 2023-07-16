package com.rainbow.server.service

import com.rainbow.server.auth.*
import com.rainbow.server.auth.jwt.JwtProvider
import com.rainbow.server.auth.security.getCurrentLoginUserId
import com.rainbow.server.config.redis.LoginInfo
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.rest.dto.member.JwtDto
import com.rainbow.server.rest.dto.member.MemberRequestDto
import com.rainbow.server.rest.dto.member.MemberResponseDto
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class KakaoLoginService(
    private val client: KakaoApiClient,
    private val memberRepository: MemberRepository,
    private val jwtProvider: JwtProvider,
    private val sessionService: SessionService,
    private val passwordEncoder: BCryptPasswordEncoder,
) {

    fun getCurrentLoginMember():Member = memberRepository.findById(getCurrentLoginUserId()).orElseThrow()

    fun getCurrentMemberInfo()=MemberResponseDto(getCurrentLoginMember())

//    @Transactional
//    fun kaKaoLogin(code:String):MemberResponseDto {
//        val accessToken = client.requestAccessToken(code)
//        val infoResponse = client.requestOauthInfo(accessToken)
//        val member=findMember(infoResponse)
////        if(member!=null) return MemberResponseDto(sessionService.storeSessionId(member).getSessionKey(),member)
//
//        return MemberResponseDto(infoResponse.email)
//
//    }

    @Transactional
    fun login(authorizedCode: String): Any {
        val accessToken = client.requestAccessToken(authorizedCode)
        val infoResponse = client.requestOauthInfo(accessToken)
        println(infoResponse.id)

        val member=findMember(infoResponse)

        /* username, password 는 아래 UsernamePasswordAuthenticationToken 의 형식을 맞춰주기 위해
        이름을 변경한 것 */
//        val username = infoResponse.kakaoAccount.profile
        val username = infoResponse.id

        // password 는 서비스에서 사용X, Security 설정을 위해 넣어준 값
        val password = username.toString()

        if(member!=null){
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(username, password)

            // JWT 발급
            return JwtDto(jwtProvider.generateToken(member))
        }


        return MemberResponseDto(nickName = infoResponse.kakaoProfile.nickname, kakaoId = username, email = infoResponse.email, birthDate = null, salary = 0.0, gender = infoResponse.kakaoAccount.gender)

    }
    @Transactional
    fun singIn(member:MemberRequestDto):JwtDto{
       val newMember= newMember(member)

        SecurityContextHolder.getContext().authentication =
            UsernamePasswordAuthenticationToken(newMember.kaKaoId, newMember.password)

        println("auth"+SecurityContextHolder.getContext().authentication)
        println("principal"+SecurityContextHolder.getContext().authentication.principal)


        // JWT 발급
        return JwtDto(jwtProvider.generateToken(newMember))

    }

    fun getById(code: String):LoginInfo?{
        return sessionService.getSessionId(code)
    }

    private fun findMember(infoResponse: KakaoInfoResponse): Member? {
        return memberRepository.findByEmail(infoResponse.email)

    }

    private fun newMember(member:MemberRequestDto): Member {
        val member = Member(
            email = member.email,
            password = passwordEncoder.encode(member.email),
            nickName = member.nickName,
            birthDate = member.birthDate,
            salary = member.salary,
            gender = member.gender,
            kaKaoId =member.kaKaoId
        )

        return memberRepository.save(member)
    }



    fun logout(): Boolean {
        return client.logout(getCurrentLoginMember().kaKaoId)
    }

}