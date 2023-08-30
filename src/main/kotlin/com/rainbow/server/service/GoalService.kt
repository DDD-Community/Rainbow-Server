package com.rainbow.server.service

import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.goal.repository.GoalRepository
import com.rainbow.server.rest.dto.goal.GoalRequestDto
import com.rainbow.server.rest.dto.goal.GoalResponseDto
import com.rainbow.server.rest.dto.goal.TotalSavedCost
import com.rainbow.server.rest.dto.goal.YearlyGoalData
import com.rainbow.server.util.logger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date

@Service
class GoalService(
    private val goalRepository: GoalRepository,
    private val memberService: MemberService,
) {

    val log = logger()

    fun createGoal(goalRequestDto: GoalRequestDto) {
        val currentMember = memberService.getCurrentLoginMember()
        val goal = Goal(
            cost = goalRequestDto.cost,
            time = goalRequestDto.yearMonth,
            member = currentMember,
            savedCost = goalRequestDto.cost,
        )
        goalRepository.save(goal)
    }

    fun getCurrentMonth(date: LocalDate): GoalResponseDto? {
        val currentMember = memberService.getCurrentLoginMember()
        return GoalResponseDto(goalRepository.findByMemberAndTime(currentMember.memberId, date))
    }

    fun updateGoal(id: Long, goalRequestDto: GoalRequestDto): GoalResponseDto {
        val goal = goalRepository.findById(id).orElseThrow()
        goal.updateCostAndSavedCost(goalRequestDto.cost)
        return GoalResponseDto(goalRepository.save(goal))
    }

    fun getSavedCost(): TotalSavedCost {
        val member = memberService.getCurrentLoginMember()
        val today = Calendar.getInstance()
        val startDate = Date.from(member.createdAt.toInstant())
        val calculateDate = (today.time.time - startDate.time) / (60 * 60 * 24 * 1000)
        val sinceDate = (calculateDate + 1).toInt()

        val goalList = member.goalList
        var savedCost = 0
        goalList.forEach { g -> savedCost += g.savedCost }

        log.info("날짜: $sinceDate")
        return TotalSavedCost(sinceSignUp = sinceDate, savedCost = savedCost)
    }


    fun getYearlyGoals(): YearlyGoalData {
        val currentMember = memberService.getCurrentLoginMember()
        val startYear = currentMember.createdAt.year
        val maxDate: LocalDate? = goalRepository.findMaxDate()

        val maxYear = maxDate?.year

        val yearDifference = maxYear?.minus(startYear)

        val yearMap = mutableMapOf<Int, List<GoalResponseDto>>()
        val totalSavedMap = mutableMapOf<Int, Int>()

        for (i in 0..(yearDifference ?: 0)) {
            val countStart = YearMonth.of(startYear + i, 1).atDay(1)
            val countEnd = YearMonth.of(startYear + i, 12).atEndOfMonth()

            val goals = goalRepository.findByMemberIdAndTimeBetween(
                countStart,
                countEnd,
                currentMember.memberId
            ).map { g -> GoalResponseDto(g) }.sortedBy { it.time }

            val sum = goals.sumOf { it.savedCost }
            yearMap[startYear + i] = goals
            totalSavedMap[startYear + i] = sum
        }

        return YearlyGoalData(yearMap, totalSavedMap)
    }
}
