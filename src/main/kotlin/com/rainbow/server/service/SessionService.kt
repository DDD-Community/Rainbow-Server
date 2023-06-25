package com.rainbow.server.service

import com.rainbow.server.config.redis.LoginInfo
import com.rainbow.server.config.redis.LoginInfoRepository
import com.rainbow.server.domain.member.entity.Member
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionService(private val loginInfoRepository: LoginInfoRepository) {

    fun storeSessionId(member: Member): LoginInfo{
        val sessionId = UUID.randomUUID().toString()
      return loginInfoRepository.save(LoginInfo(sessionId,member.nickName,member.email))
    }

    fun getSessionId(sessionId: String): LoginInfo? {
        return loginInfoRepository.findById(sessionId).orElse(null)
    }

    fun logOut(sessionId: String) {
        return loginInfoRepository.deleteById(sessionId)
    }

//    private fun getSessionKey(sessionId: String): String {
//        return "$SESSION_PREFIX$sessionId"
//    }
}
