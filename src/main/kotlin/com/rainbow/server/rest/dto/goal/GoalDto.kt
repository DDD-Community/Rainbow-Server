package com.rainbow.server.rest.dto.goal

import com.rainbow.server.domain.goal.entity.Goal
import java.time.LocalDateTime

data class GoalRequestDto(
  val memberId:Long,
    val yearMonth: LocalDateTime,
    var cost: Long,
    var id:Long=0
)

data class GoalResponseDto(
    var cost:Long,
    var percentage:Double,
    var id:Long
){
    constructor(goal: Goal):this(
        cost=goal.cost,
        percentage=goal.percentage,
        id=goal.id
    )
}