package com.rainbow.server.rest.dto.goal

import com.rainbow.server.domain.goal.entity.Goal
import java.time.LocalDate

data class GoalRequestDto(
    val memberId: Int,
    val yearMonth: LocalDate,
    var cost: Int,
    var id: Long = 0
)

data class GoalResponseDto(
    var cost: Int,
    var paidAmount: Int,
    var goalId: Long,
    var time: LocalDate,
    var savedCost: Int,

) {

    constructor(goal: Goal) : this(
        cost = goal.cost,
        paidAmount = goal.paidAmount,
        goalId = goal.goalId,
        time = goal.time,
        savedCost = goal.savedCost
    )
}

data class TotalSavedCost(
    var sinceSignUp: Int,
    var savedCost: Int,
)
