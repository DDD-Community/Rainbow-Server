package com.rainbow.server.service

import com.rainbow.server.auth.KakaoInfoResponse
import com.rainbow.server.config.redis.RefreshToken
import com.rainbow.server.config.redis.RefreshTokenRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService(private val refreshTokenRepository: RefreshTokenRepository) {
    companion object {
        private const val SESSION_PREFIX = "session:"
    }

    fun storeSessionId(infoResponse: KakaoInfoResponse): RefreshToken{
        val sessionId = UUID.randomUUID().toString()
        val sessionKey = getSessionKey(sessionId)
      return refreshTokenRepository.save(RefreshToken(sessionKey,infoResponse.nickname,infoResponse.email))
    }

    fun getSessionId(sessionId: String): RefreshToken? {
        val sessionKey = getSessionKey(sessionId)
        return refreshTokenRepository.findById(sessionKey).orElse(null)
    }

    private fun getSessionKey(sessionId: String): String {
        return "$SESSION_PREFIX$sessionId"
    }
}
