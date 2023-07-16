package com.rainbow.server.rest.dto.member

import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate

data class MemberRequestDto(
    val email: String,
    val nickName:String,
    val birthDate: LocalDate,
    val salary:Double,
    val gender: String,
    val kaKaoId:Long
)
data class JwtDto(val accessToken: String?)
data class MemberResponseDto(
    val email: String,
    val nickName:String?,
//    val ageRange: String?,
    val birthDate: LocalDate?,
    val gender: String?,
    val salary: Double?,
    val kakaoId:Long
){
//    constructor (email: String) : this(email, null, null,null,null)
    constructor(member: Member):this(
        email=member.email,
        nickName=member.nickName,
        birthDate=member.birthDate,
        gender=member.gender,
        salary=member.salary,
        kakaoId=member.kaKaoId
    )

}