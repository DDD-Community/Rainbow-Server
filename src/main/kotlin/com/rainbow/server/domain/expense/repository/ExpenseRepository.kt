package com.rainbow.server.domain.expense.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.entity.QDailyExpense.dailyExpense
import com.rainbow.server.domain.expense.entity.QExpense.expense
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.entity.QMember.member
import com.rainbow.server.rest.dto.expense.ExpenseResponse
import com.rainbow.server.rest.dto.expense.FriendsExpenseDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ExpenseRepository : JpaRepository<Expense, Long>, CustomExpenseRepository

interface CustomExpenseRepository {
    fun getAllExpensesByContentAndDateBetween(content: String, startDate: LocalDate, endDate: LocalDate, member: Member): List<Expense>?

    fun getFriendsExpenseList(lastId: Long?, followingMembers: List<Member>): List<FriendsExpenseDto>?
}

class ExpenseRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomExpenseRepository {
    override fun getAllExpensesByContentAndDateBetween(content: String, startDate: LocalDate, endDate: LocalDate, member: Member): List<Expense>? {
        return queryFactory.selectFrom(expense).where(
            (expense.dailyExpense.member.memberId.eq(member.memberId)).and(
                expense.content.contains(content).and(expense.dailyExpense.date.between(startDate, endDate)),
            ),
        ).fetch()
    }

    override fun getFriendsExpenseList(lastId: Long?, followingMembers: List<Member>): List<FriendsExpenseDto>? {
        val query = getCommonQuery()
        if (lastId != null) {
            query.where(expense.expenseId.lt(lastId)) // 지정한 lastId보다 작은 ID를 가진 데이터만 가져오도록 조건 추가
        }

        val result = mutableListOf<FriendsExpenseDto>()

        val friendsExpensesQuery = query.where(member.`in`(followingMembers)).limit(4)
        val friendsExpenses = friendsExpensesQuery.fetch()
        result.addAll(friendsExpenses)

        val nonFollowingQuery = getCommonQuery().where(member.notIn(followingMembers)).limit(1)
        val nonFollowingExpense = nonFollowingQuery.fetchOne()
        nonFollowingExpense?.let {
            result.add(it)
        }
        return result
    }

    private fun getCommonQuery(): JPAQuery<FriendsExpenseDto> {
        return queryFactory
            .select(
                Projections.constructor(
                    FriendsExpenseDto::class.java,
                    member.memberId,
                    member.nickName,
                    Projections.constructor(
                        ExpenseResponse::class.java,
                        expense,
                    ),
                    dailyExpense.date
                )
            )
            .from(expense)
            .join(dailyExpense).on(expense.dailyExpense.dailyExpenseId.eq(dailyExpense.dailyExpenseId))
            .join(member).on(dailyExpense.member.memberId.eq(member.memberId))
            .orderBy(expense.expenseId.desc())
            .orderBy(expense.createdAt.desc())
    }
}
