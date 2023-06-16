package com.rainbow.server.service

import com.rainbow.server.auth.*
import com.rainbow.server.config.redis.RefreshToken
import com.rainbow.server.domain.member.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class KakaoLoginService(
    private val client: KakaoApiClient,
    private val memberRepository: MemberRepository,
    private val sessionService: SessionService
) {

    fun login(code:String): RefreshToken {
        val accessToken = client.requestAccessToken(code)
        val infoResponse = client.requestOauthInfo(accessToken)
       findOrCreateMember(infoResponse)
        sessionService.storeSessionId(infoResponse)
        println(infoResponse)
        return sessionService.storeSessionId(infoResponse)
    }

    fun getById(code: String):RefreshToken?{
        return sessionService.getSessionId(code)
    }

    private fun findOrCreateMember(infoResponse: KakaoInfoResponse): Long {
        return memberRepository.findByEmail(infoResponse.email)  ?.let { it.id }
            ?: newMember(infoResponse)
    }

    private fun newMember(infoResponse: KakaoInfoResponse): Long {
        val member = Member(
            email = infoResponse.email,
            nickName = infoResponse.nickname,
        )

        return memberRepository.save(member).id
    }

    fun logout(code: String): KakaoUserLogout {
        return client.logout(code)
    }

}
