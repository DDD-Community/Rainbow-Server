package com.rainbow.server.rest.dto.member

import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.entity.Salary
import java.time.LocalDate

data class MemberRequestDto(
    val email: String,
    val nickName:String,
    val birthDate: LocalDate,
    val salary: Int,
    val gender: String,
    val kaKaoId:Long
)
data class JwtDto(val accessToken: String?,
    val refreshToken:String?)
data class MemberResponseDto(
    val email: String,
    val nickName:String?,
//    val ageRange: String?,
    val birthDate: LocalDate?,
    val gender: String?,
    val salary: Int?,
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

data class DuplicateCheck(
    var data:String
)

data class SalaryDto(
    val salaryRange:String,
    val idx:Int
){
    constructor(salary:Salary):this(
        salaryRange=salary.salaryRange,
        idx=salary.idx
    )
}