package com.rainbow.server.config.redis

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import org.springframework.data.annotation.Id

@RedisHash(value = "sessionKey", timeToLive = 3660)
data class LoginInfo(
    @Id
    private val sessionKey: String,
    val memberName: String,
    val email: String
) : Serializable {
    @JsonIgnore
    fun getSessionKey(): String {
        return sessionKey
    }
}