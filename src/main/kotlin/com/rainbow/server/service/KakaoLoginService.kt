package com.rainbow.server.service

import com.rainbow.server.auth.*
import com.rainbow.server.config.redis.LoginInfo
import com.rainbow.server.domain.member.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class KakaoLoginService(
    private val client: KakaoApiClient,
    private val memberRepository: MemberRepository,
    private val sessionService: SessionService
) {

    fun login(code:String): LoginInfo {
        val accessToken = client.requestAccessToken(code)
        val infoResponse = client.requestOauthInfo(accessToken)
        println(infoResponse)
        return sessionService.storeSessionId(findOrCreateMember(infoResponse))
    }

    fun getById(code: String):LoginInfo?{
        return sessionService.getSessionId(code)
    }

    private fun findOrCreateMember(infoResponse: KakaoInfoResponse): Member {
        return memberRepository.findByEmail(infoResponse.email)  ?.let { it }
            ?: newMember(infoResponse)
    }

    private fun newMember(infoResponse: KakaoInfoResponse): Member {
        val member = Member(
            email = infoResponse.email,
            nickName = infoResponse.nickname,
        )

        return memberRepository.save(member)
    }

    fun logout(code: String): KakaoUserLogout {
        return client.logout(code)
    }

}
