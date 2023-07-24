package com.rainbow.server.domain.goal.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.goal.entity.Goal
import com.rainbow.server.domain.goal.entity.QGoal.goal
import com.rainbow.server.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/***
 * 예시 Repository
 * */
@Repository
interface GoalRepository: JpaRepository<Goal, Long> ,GoalCustomRepository{

}

interface GoalCustomRepository{
    fun findAllYears(member: Member):List<LocalDate>

    fun findByMemberIdAndTimeBetween(startDate:LocalDate,endDate:LocalDate,memberId: Long):List<Goal>

    fun findMaxDate(): LocalDate?
}


class GoalRepositoryImpl (private val queryFactory: JPAQueryFactory
):GoalCustomRepository{
    override fun findAllYears(member: Member): List<LocalDate> {
     return  queryFactory.selectDistinct(goal.time)
           .where(goal.member.eq(member))
           .fetch()
    }

    override fun findMaxDate(): LocalDate? {
        return queryFactory.select(goal.time.max())
            .from(goal)
            .fetchOne()
    }

    override fun findByMemberIdAndTimeBetween(startDate: LocalDate, endDate: LocalDate, memberId: Long): List<Goal> {
        return queryFactory.selectFrom(goal)
            .where(goal.time.between(startDate,endDate).and(goal.member.memberId.eq(memberId)))
            .fetch()
    }
}