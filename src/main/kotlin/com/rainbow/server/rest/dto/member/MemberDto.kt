package com.rainbow.server.rest.dto.member

import com.rainbow.server.domain.member.entity.Member

data class MemberRequestDto(
    val email: String,
    val nickName:String,
    val sessionKey: String,
    val ageRange: String,
    val gender: String,
)

data class MemberResponseDto(
    val email: String,
    val sessionKey: String?,
    val nickName:String?,
    val ageRange: String?,
    val gender: String?
){
    constructor (email: String) : this(email, null, null,null,null)
    constructor(sessionKey: String?,member: Member):this(
        email=member.email,
        sessionKey=sessionKey,
        nickName=member.nickName,
        ageRange = member.ageRange,
        gender=member.gender
    )




}