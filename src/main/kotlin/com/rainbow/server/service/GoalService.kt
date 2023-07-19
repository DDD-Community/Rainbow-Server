package com.rainbow.server.service

import com.rainbow.server.auth.security.getCurrentLoginUserId
import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.goal.repository.GoalRepository
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.rest.dto.goal.GoalRequestDto
import com.rainbow.server.rest.dto.goal.GoalResponseDto
import org.springframework.stereotype.Service

@Service
class GoalService (private val goalRepository: GoalRepository,
    private val memberRepository: MemberRepository){

    fun createGoal(goalRequestDto: GoalRequestDto){
        val member= memberRepository.findById(getCurrentLoginUserId()).orElseThrow()

        val goal= Goal(
            cost = goalRequestDto.cost,
            time = goalRequestDto.yearMonth,
            member =member
        )
        goalRepository.save(goal)
    }

    fun updateGoal(goalRequestDto: GoalRequestDto): GoalResponseDto {
        val goal=goalRepository.findById(goalRequestDto.id).orElseThrow()
        goal.updateCost(goalRequestDto.cost)
        return GoalResponseDto(goalRepository.save(goal))
    }
}