package com.rainbow.server.rest.dto.member

import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.entity.Salary
import com.rainbow.server.rest.dto.expense.DailyExpenseResponse
import java.time.LocalDate
import kotlin.streams.toList

data class MemberRequestDto(
    val email: String,
    val nickName: String,
    val birthDate: LocalDate,
    val salary: Int,
    val gender: String,
    val kaKaoId: Long,
)

data class JwtDto(
    val accessToken: String?,
    val refreshToken: String?,
)

data class MemberResponseDto(
    val email: String,
    val nickName: String?,
//    val ageRange: String?,
    val birthDate: LocalDate?,
    val gender: String?,
    val salary: Int?,
    val kakaoId: Long,
) {
    //    constructor (email: String) : this(email, null, null,null,null)
    constructor(member: Member) : this(
        email = member.email,
        nickName = member.nickName,
        birthDate = member.birthDate,
        gender = member.gender,
        salary = member.salary,
        kakaoId = member.kaKaoId,
    )
}

data class FriendSearchResponse(
    val memberId: Long,
    val isFriend: Boolean,
    val nickName: String,
) {
    constructor(member: Member, isFriend: Boolean) : this(
        memberId = member.memberId,
        isFriend = isFriend,
        nickName = member.nickName,
    )
}

data class FriendDetailResponse(
    val monthlyPaidAmount: Int?,
    val dailyExpenseList: List<DailyExpenseResponse>?,
    val memberInfo: MemberResponseDto,
    val isFriend: Boolean,
) {
    constructor(member: Member, isFriend: Boolean, goal: Goal?) : this(
        monthlyPaidAmount = goal?.paidAmount,
        dailyExpenseList = goal?.dailyExpenseList?.stream()?.map { d -> DailyExpenseResponse(d) }?.toList(),
        memberInfo = MemberResponseDto(member),
        isFriend = isFriend,
    )
}

data class CheckDuplicateResponse(val isDuplicated: Boolean)

data class FollowingRequest(val followingId: Long)

data class SalaryDto(
    val salaryRange: String,
    val idx: Int,
    val salaryId: Long,
) {
    constructor(salary: Salary) : this(
        salaryRange = salary.salaryRange,
        idx = salary.idx,
        salaryId = salary.salaryId,
    )
}
