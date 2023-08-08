package com.rainbow.server.config.redis

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.io.Serializable

@RedisHash(value = "sessionKey", timeToLive = 36600000)
data class RefreshToken(
    @Id
    @Indexed
    val refreshToken: String,
    val memberId: Long,
) : Serializable
