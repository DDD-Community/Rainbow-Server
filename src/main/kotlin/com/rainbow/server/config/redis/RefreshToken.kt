package com.rainbow.server.config.redis

import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import org.springframework.data.annotation.Id

@RedisHash(value = "sessionKey", timeToLive = 3660)
data class RefreshToken(
    @Id
    val sessionKey: String,
    val memberName:String,
    val email:String
) : Serializable