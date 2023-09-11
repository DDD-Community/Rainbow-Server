package com.rainbow.server.rest.dto.member

import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.entity.Salary
import com.rainbow.server.rest.dto.expense.ExpenseResponse
import java.time.LocalDate
import java.util.SortedMap

data class MemberRequestDto(
    val email: String,
    val nickName: String,
    val birthDate: LocalDate,
    val salary: String,
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
    val salary: String?,
    val kaKaoId: Long,
    val imagePath: String? = null,
    val id: Long,
) {
    //    constructor (email: String) : this(email, null, null,null,null)
    constructor(member: Member) : this(
        email = member.email,
        nickName = member.nickName,
        birthDate = member.birthDate,
        gender = member.gender,
        salary = member.salary,
        kaKaoId = member.kaKaoId,
        imagePath = member.imagePath,
        id = member.memberId,
    )
}

data class ConditionFilteredMembers(
    val condition: String, // "age", "salary", "topExpense", ...
    val members: List<MemberResponseDto>,
)

data class FriendSearchResponse(
    val memberId: Long,
    val isFriend: Boolean,
    val nickName: String,
    val imagePath: String?,
) {
    constructor(member: Member, isFriend: Boolean) : this(
        memberId = member.memberId,
        isFriend = isFriend,
        nickName = member.nickName,
        imagePath = member.imagePath,
    )
}

data class FriendDetailResponse(
    val monthlyPaidAmount: Int?,
//    val dailyExpenseList: List<DailyExpenseResponse>?,
    val expenseMap: SortedMap<LocalDate?, List<ExpenseResponse>>?,
    val memberInfo: MemberResponseDto,
    val isFriend: Boolean,
) {
    constructor(member: Member, expenseMap: SortedMap<LocalDate?, List<ExpenseResponse>>?, isFriend: Boolean, goal: Goal?) : this(
        monthlyPaidAmount = goal?.paidAmount,
        expenseMap = expenseMap,
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
