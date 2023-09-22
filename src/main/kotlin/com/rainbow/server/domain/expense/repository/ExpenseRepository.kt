package com.rainbow.server.domain.expense.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.entity.ExpenseReview
import com.rainbow.server.domain.expense.entity.QDailyExpense.dailyExpense
import com.rainbow.server.domain.expense.entity.QExpense.expense
import com.rainbow.server.domain.expense.entity.QExpenseReview.expenseReview
import com.rainbow.server.domain.expense.entity.QReview.review
import com.rainbow.server.domain.expense.entity.Review
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

    fun getFriendsFeedList(lastId: Long?, member: Member, followingMembers: List<Member>): List<FriendsExpenseDto>?

    fun getAnotherMemberExpenseList(memberId: Long, pageSize: Long, pageNum: Long): List<Expense>?
}

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByEmojiName(emojiNAme: String): Review?
}

interface ExpenseReviewRepository : JpaRepository<ExpenseReview, Long>, CustomExpenseReviewRepository

interface CustomExpenseReviewRepository {
    fun getAllReviewsByExpense(expenseId: Long): List<Review>
}

class ExpenseReviewRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
) : CustomExpenseReviewRepository {
    override fun getAllReviewsByExpense(expenseId: Long): List<Review> {
        return queryFactory
            .select(review)
            .from(expenseReview)
            .join(expenseReview.review, review)
            .where(expenseReview.expense.expenseId.eq(expenseId))
            .fetch()
    }
}

class ExpenseRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val expenseReviewRepository: ExpenseReviewRepository,
) : CustomExpenseRepository {

    override fun getAllExpensesByContentAndDateBetween(content: String, startDate: LocalDate, endDate: LocalDate, member: Member): List<Expense>? {
        return queryFactory.selectFrom(expense).where(
            (expense.dailyExpense.member.memberId.eq(member.memberId)).and(
                expense.content.contains(content).and(expense.dailyExpense.date.between(startDate, endDate)),
            ),
        ).fetch()
    }

    override fun getFriendsFeedList(lastId: Long?, currentMember: Member, followingMembers: List<Member>): List<FriendsExpenseDto>? {
        val query = getCommonQuery()
        if (lastId != null) {
            query.where(expense.expenseId.lt(lastId)) // 지정한 lastId보다 작은 ID를 가진 데이터만 가져오도록 조건 추가
        }

        val result = mutableListOf<FriendsExpenseDto>()

        val friendsExpensesQuery = query.where(member.`in`(followingMembers)).orderBy(expense.expenseId.desc())
            .orderBy(expense.createdAt.desc()).limit(4)
        val friendsExpenses = friendsExpensesQuery.fetch()
        result.addAll(friendsExpenses)

        val notFollowingMembers = followingMembers.toMutableList()
        notFollowingMembers.add(currentMember)
        val nonFollowingQuery = getCommonQuery().where(member.notIn(notFollowingMembers.toList())).limit(1)
        val nonFollowingExpense = nonFollowingQuery.fetchOne()
        nonFollowingExpense?.let {
            it.updateIsFriend(false)
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
                    dailyExpense.date,
                    member.imagePath,

                ),
            )
            .from(expense)
            .join(dailyExpense).on(expense.dailyExpense.dailyExpenseId.eq(dailyExpense.dailyExpenseId))
            .join(member).on(dailyExpense.member.memberId.eq(member.memberId))
    }

    override fun getAnotherMemberExpenseList(memberId: Long, pageSize: Long, pageNum: Long): List<Expense>? {
        return queryFactory
            .selectFrom(expense)
            .join(expense.dailyExpense, dailyExpense)
            .where((dailyExpense.member.memberId.eq(memberId)).and(expense.customCategory.status.eq(true)))
            .orderBy(dailyExpense.date.desc(), expense.createdAt.asc())
            .limit(pageSize)
            .offset(pageNum)
            .fetch()
    }
}
