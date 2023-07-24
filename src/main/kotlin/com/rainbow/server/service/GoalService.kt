package com.rainbow.server.service

import com.rainbow.server.auth.security.getCurrentLoginUserId
import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.goal.repository.GoalRepository
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.rest.dto.goal.GoalRequestDto
import com.rainbow.server.rest.dto.goal.GoalResponseDto
import com.rainbow.server.rest.dto.goal.TotalSavedCost
import com.rainbow.server.util.logger
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.YearMonth
import java.util.Calendar
import java.util.Date
import kotlin.streams.toList

@Service
class GoalService (private val goalRepository: GoalRepository,
    private val memberRepository: MemberRepository){

    val log = logger()
    fun getCurrentLoginMember(): Member = memberRepository.findById(getCurrentLoginUserId()).orElseThrow()


    fun createGoal(goalRequestDto: GoalRequestDto){
        val member= getCurrentLoginMember()

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

    fun getSavedCost(): TotalSavedCost {
        val member=getCurrentLoginMember()
        val today=Calendar.getInstance()
        val startDate=Date.from(member.createdAt.toInstant())
        val calculateDate=(today.time.time - startDate.time)/(60 * 60 * 24 * 1000)
        val sinceDate=(calculateDate+1).toInt()

        var goalList=member.goalList
        var savedCost=0
        goalList.stream().map { g->{
            savedCost+=g.cost-g.paidAmount
        } }

        log.info("날짜: $sinceDate")
        return TotalSavedCost(sinceSignUp  =sinceDate, savedCost = savedCost)
    }

    fun getYearlyGoals():HashMap<Int, List<GoalResponseDto>>{
//        var goalList=getCurrentLoginMember().goalList
//        var yearList=goalRepository.findAllYears(getCurrentLoginMember())
        val member=getCurrentLoginMember()
        val startYear=getCurrentLoginMember().createdAt.year
        val maxDate: LocalDate? = goalRepository.findMaxDate()

        val maxYear=maxDate?.year

        var startCountYear=startYear

        val yearDifference= maxYear?.minus(startYear)

        var yearMap= HashMap<Int, List<GoalResponseDto>>()
        for(i:Int in 0..yearDifference!!){
            val countStart=YearMonth.of(startCountYear,1).atDay(1)
            val countEnd=YearMonth.of(startCountYear, 12).atEndOfMonth()
            yearMap[startCountYear] =goalRepository.findByMemberIdAndTimeBetween(countStart,countEnd,member.memberId).stream().map { g->GoalResponseDto(g) }.toList().sortedBy { it.time }
            startCountYear++
        }



    return yearMap


    }



}