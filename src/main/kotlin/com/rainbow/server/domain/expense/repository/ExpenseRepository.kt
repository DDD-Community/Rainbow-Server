package com.rainbow.server.domain.expense.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.entity.ExpenseReview
import com.rainbow.server.domain.expense.entity.QExpense.expense
import com.rainbow.server.domain.expense.entity.Review
import com.rainbow.server.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository : JpaRepository<Expense, Long>, CustomExpenseRepository

interface CustomExpenseRepository {
    fun getAllExpensesByContent(content: String, member: Member): List<Expense>?
}

interface ReviewRepository : JpaRepository<Review, Long> {}

interface ExpenseReviewRepository : JpaRepository<ExpenseReview, Long> {}

class ExpenseRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomExpenseRepository {
    override fun getAllExpensesByContent(content: String, member: Member): List<Expense>? {
        return queryFactory.selectFrom(expense).where(
            (expense.dailyExpense.member.memberId.eq(member.memberId)).and(
                expense.content.contains(content),
            ),
        ).fetch()
    }
}
