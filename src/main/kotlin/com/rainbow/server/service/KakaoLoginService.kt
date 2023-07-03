package com.rainbow.server.service

import com.rainbow.server.auth.*
import com.rainbow.server.config.redis.LoginInfo
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.rest.dto.member.MemberRequestDto
import com.rainbow.server.rest.dto.member.MemberResponseDto
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class KakaoLoginService(
    private val client: KakaoApiClient,
    private val memberRepository: MemberRepository,
    private val sessionService: SessionService
) {

    @Transactional
    fun kaKaoLogin(code:String):MemberResponseDto {
        val accessToken = client.requestAccessToken(code)
        val infoResponse = client.requestOauthInfo(accessToken)
        val member=findOrCreateMember(infoResponse)
        if(member!=null) return MemberResponseDto(sessionService.storeSessionId(member).getSessionKey(),member)

        return MemberResponseDto(infoResponse.email)

    }
    @Transactional
    fun singIn(member:MemberRequestDto):MemberResponseDto{
       val newMember= newMember(member)
        return MemberResponseDto(sessionService.storeSessionId(newMember).getSessionKey(),newMember)
    }

    fun getById(code: String):LoginInfo?{
        return sessionService.getSessionId(code)
    }

    private fun findOrCreateMember(infoResponse: KakaoInfoResponse): Member? {
        return memberRepository.findByEmail(infoResponse.email)

    }

    private fun newMember(member:MemberRequestDto): Member {
        val member = Member(
            email = member.email,
            nickName = member.nickName,
            ageRange = member.ageRange,
            gender = member.gender
        )

        return memberRepository.save(member)
    }

    fun logout(sessionKey: String)  {
        return sessionService.logOut(sessionKey)
    }

//    fun logout(code: String): KakaoUserLogout {
//        return client.logout(code)
//    }

}