package com.rainbow.server.domain.member.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.expense.entity.QDailyExpense.dailyExpense
import com.rainbow.server.domain.expense.entity.QExpense.expense
import com.rainbow.server.domain.goal.entity.QGoal.goal
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.entity.QMember.member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

/***
 * 예시 Repository
 * */
@Repository
interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom {
    fun findByEmail(email: String): Member?

    fun existsByEmail(email: String): Boolean
    fun existsByNickName(nickName: String): Boolean
}

interface MemberRepositoryCustom {
    fun findSuggestedMemberList(standardMember: Member): List<Member>

    fun findMemberListBySalary(salaryStart: Int, salaryEnd: Int): List<Member>

    fun findAllByNickName(nickName: String): List<Member>?

    fun findNewbies(): List<Member>

    fun getAgePredicate(birthDate: LocalDate): BooleanExpression

    fun getSalaryPredicate(salary: Int): BooleanExpression

    fun fetchMembersForCondition(predicate: BooleanExpression, excludeMembers: Set<Member>, limit: Int, currentMember: Member): List<Member>

    fun topExpenseCreators(excludeMembers: Set<Member>, maxRecommendedPerCategory: Int): List<Member>

    fun fetchRecentJoinMembers(existingMembers: Set<Member>, count: Int, currentMember: Member): List<Member>
}

class MemberRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : MemberRepositoryCustom {

    override fun findSuggestedMemberList(standardMember: Member): List<Member> {
        return queryFactory.selectFrom(member)
            .where(
                (member.salary.eq(standardMember.salary))
                    .or(member.birthDate.eq(standardMember.birthDate))
            )
            .fetch()
    }

    override fun findAllByNickName(nickName: String): List<Member>? {
        return queryFactory.selectFrom(member)
            .where(member.nickName.contains(nickName))
            .fetch()
    }
    override fun findMemberListBySalary(salaryStart: Int, salaryEnd: Int): List<Member> {
        return queryFactory.select(member)
            .from(member)
            .limit(5)
            .where(member.salary.eq(salaryStart))
            .fetch()
    }

    override fun findNewbies(): List<Member> {
        return queryFactory.selectFrom(member)
            .limit(10)
            .orderBy(member.createdAt.desc())
            .fetch()
    }

    override fun fetchMembersForCondition(predicate: BooleanExpression, excludeMembers: Set<Member>, limit: Int, currentMember: Member): List<Member> {
        val excludedMemberIds = excludeMembers.map { it.memberId } + currentMember.memberId
        return queryFactory.selectFrom(member)
            .where(predicate.and(member.memberId.notIn(excludedMemberIds)))
            .limit(limit.toLong())
            .fetch()
    }

    override fun getAgePredicate(birthDate: LocalDate): BooleanExpression {
        return member.birthDate.between(birthDate.minusYears(2), birthDate.plusYears(2))
    }

    override fun getSalaryPredicate(salary: Int): BooleanExpression {
        return member.salary.eq(salary)
    }

    override fun topExpenseCreators(excludeMembers: Set<Member>, maxRecommendedPerCategory: Int): List<Member> {
        return queryFactory.selectFrom(member)
            .join(member.goalMutableList, goal)
            .join(goal.dailyExpenseMutableList, dailyExpense)
            .join(dailyExpense.expenseMutableList, expense)
            .groupBy(member)
            .orderBy(dailyExpense.expenseMutableList.size().desc())
            .where(member.notIn(excludeMembers))
            .limit(maxRecommendedPerCategory.toLong())
            .fetch()
    }

    override fun fetchRecentJoinMembers(existingMembers: Set<Member>, count: Int, currentMember: Member): List<Member> {
        val excludedMemberIds = existingMembers.map { it.memberId } + currentMember.memberId
        return queryFactory
            .selectFrom(member)
            .where(member.memberId.notIn(excludedMemberIds))
            .orderBy(member.createdAt.desc())
            .limit(count.toLong())
            .fetch()
    }
}
