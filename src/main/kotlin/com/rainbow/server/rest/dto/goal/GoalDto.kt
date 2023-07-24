package com.rainbow.server.rest.dto.goal

import com.rainbow.server.domain.goal.entity.Goal
import java.time.LocalDate

data class GoalRequestDto(
    val memberId:Int,
    val yearMonth: LocalDate,
    var cost: Int,
    var id:Long=0
)

data class GoalResponseDto(
    var cost:Int,
    var paidAmount:Int,
    var id:Long,
    var time:LocalDate
){
    constructor(goal: Goal):this(
        cost=goal.cost,
        paidAmount=goal.paidAmount,
        id=goal.id,
        time=goal.time
    )
}

data class TotalSavedCost(
    var sinceSignUp: Int,
    var savedCost: Int
)


data class YearlyGoals(
    var year: Int,
    var savedCost: Int
)