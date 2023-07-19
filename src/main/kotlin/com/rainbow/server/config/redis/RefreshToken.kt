package com.rainbow.server.config.redis

import com.fasterxml.jackson.annotation.JsonIgnore
import com.rainbow.server.domain.member.entity.Member
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "sessionKey", timeToLive = 36600000)
data class RefreshToken(
    @Id
    @Indexed
    val refreshToken: String,
    val memberId: Long
) : Serializable