package com.rainbow.server.service

import com.rainbow.server.config.redis.LoginInfo
import com.rainbow.server.config.redis.LoginInfoRepository
import com.rainbow.server.domain.member.entity.Member
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService(private val loginInfoRepository: LoginInfoRepository) {
    companion object {
        private const val SESSION_PREFIX = "session:"
    }

    fun storeSessionId(member: Member): LoginInfo{
        val sessionId = UUID.randomUUID().toString()
        val sessionKey = getSessionKey(sessionId)
      return loginInfoRepository.save(LoginInfo(sessionKey,member.nickName,member.email))
    }

    fun getSessionId(sessionId: String): LoginInfo? {
        val sessionKey = getSessionKey(sessionId)
        return loginInfoRepository.findById(sessionKey).orElse(null)
    }

    private fun getSessionKey(sessionId: String): String {
        return "$SESSION_PREFIX$sessionId"
    }
}
