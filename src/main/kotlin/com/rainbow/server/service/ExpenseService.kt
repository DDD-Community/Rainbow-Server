package com.rainbow.server.service

import com.rainbow.server.domain.expense.entity.DailyExpense
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.entity.Review
import com.rainbow.server.domain.expense.repository.CustomCategoryRepository
import com.rainbow.server.domain.expense.repository.DailyExpenseRepository
import com.rainbow.server.domain.expense.repository.ExpenseRepository
import com.rainbow.server.domain.expense.repository.ExpenseReviewRepository
import com.rainbow.server.domain.expense.repository.ReviewRepository
import com.rainbow.server.domain.goal.repository.GoalRepository
import com.rainbow.server.exception.CustomException
import com.rainbow.server.exception.ErrorCode
import com.rainbow.server.rest.dto.expense.CreateReviewRequest
import com.rainbow.server.rest.dto.expense.CustomCategoryExpenseListResponse
import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
import com.rainbow.server.rest.dto.expense.CustomCategoryResponse
import com.rainbow.server.rest.dto.expense.DailyCharacter
import com.rainbow.server.rest.dto.expense.DailyExpenseResponse
import com.rainbow.server.rest.dto.expense.ExpenseRequest
import com.rainbow.server.rest.dto.expense.ExpenseResponse
import com.rainbow.server.rest.dto.expense.UpdateDailyExpenseRequest
import com.rainbow.server.rest.dto.expense.UpdateExpenseRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.streams.toList

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val dailyExpenseRepository: DailyExpenseRepository,
    private val goalRepository: GoalRepository,
    private val customCategoryRepository: CustomCategoryRepository,
    private val memberService: MemberService,
    private val reviewRepository: ReviewRepository,
    private val expenseReviewRepository: ExpenseReviewRepository,
) {

    private val maxCategorySize: Int = 30

    @Transactional
    fun createExpense(expenseRequest: ExpenseRequest) {
        val currentMember = memberService.getCurrentLoginMember()
        val goal = goalRepository.findByMemberAndTime(currentMember.memberId, expenseRequest.date.withDayOfMonth(1))
        goal.updatePaidAmountAndSavedCost(expenseRequest.amount)
        val customCategory = customCategoryRepository.findById(expenseRequest.categoryId).orElseThrow()
        val dailyExpense = dailyExpenseRepository.findById(expenseRequest.dailyExpenseId).orElseThrow()
        val expense = Expense(
            amount = expenseRequest.amount,
            content = expenseRequest.content,
            customCategory = customCategory,
            dailyExpense = dailyExpense,
            memo = expenseRequest.memo,
        )
        dailyExpense.addExpense(expense)
        dailyExpenseRepository.save(dailyExpense)
    }

    @Transactional
    fun createCustomCategory(customCategoryRequest: CustomCategoryRequest) {
        val currentMember = memberService.getCurrentLoginMember()
        customCategoryRepository.save(customCategoryRequest.to(currentMember))
    }

    fun getAllMyCustomCategory(): List<CustomCategoryResponse>? {
        val currentMember = memberService.getCurrentLoginMember()
        return currentMember.customCategoryList.stream().map { c -> CustomCategoryResponse(c) }.toList()
    }

    fun updateCustomCategory(id: Long, customCategoryRequest: CustomCategoryRequest) {
        val customCategory = customCategoryRepository.findById(id).orElseThrow()
        customCategory.updateCustomCategory(customCategoryRequest)
        customCategoryRepository.save(customCategory)
    }

    fun countCustomCategory(): Boolean {
        val currentMember = memberService.getCurrentLoginMember()
        return currentMember.customCategoryList.size < maxCategorySize
    }

    fun getCustomCategory(categoryId: Long): CustomCategoryExpenseListResponse {
        return CustomCategoryExpenseListResponse(customCategoryRepository.findById(categoryId).orElseThrow())
    }

    fun modifyExpense(expenseRequest: UpdateExpenseRequest) {
        val expense = expenseRepository.findById(expenseRequest.id).orElseThrow()
        val goal = expense.dailyExpense.goal
        goal.modifyPaidAmountAndSavedCost(expense.amount, expenseRequest.amount)
        expense.modifyExpense(expenseRequest.amount, expenseRequest.content, expenseRequest.memo)
        goalRepository.save(goal)
        expenseRepository.save(expense)
    }

    @Transactional
    fun createOrReadDailyExpense(date: LocalDate): DailyExpenseResponse {
        val currentMember = memberService.getCurrentLoginMember()
        val goal = goalRepository.findByMemberAndTime(currentMember.memberId, date.withDayOfMonth(1))
        val dailyExpense = dailyExpenseRepository.findByDateAndMember(date, currentMember) ?: DailyExpense(
            member = currentMember,
            goal = goal,
            date = date,
        )
        goal.addDailyExpenseList(dailyExpense)
        goalRepository.save(goal)
        return DailyExpenseResponse(dailyExpense)
    }

    fun updateDailyCharacterAndComment(id: Long, updateDailyExpenseRequest: UpdateDailyExpenseRequest) {
        val dailyExpense = dailyExpenseRepository.findById(id).orElseThrow()
        dailyExpense.updateCharacterAndComment(updateDailyExpenseRequest)
        dailyExpenseRepository.save(dailyExpense)
    }

    fun getAllDaysCharacters(date: LocalDate): List<DailyCharacter>? {
        val currentMember = memberService.getCurrentLoginMember()
        val dailyExpenseList = dailyExpenseRepository.findAllByMemberAndDateBetween(
            currentMember,
            date,
            date.with(TemporalAdjusters.lastDayOfMonth()),
        )
        return dailyExpenseList?.stream()?.map { e -> DailyCharacter(e) }?.toList()?.sortedBy { it.date }
    }

    fun getAllExpensesByContent(date: LocalDate, content: String): List<ExpenseResponse>? {
        val currentMember = memberService.getCurrentLoginMember()
        val expenseList = expenseRepository.getAllExpensesByContentAndDateBetween(
            content,
            date,
            date.with(TemporalAdjusters.lastDayOfMonth()),
            currentMember,
        )
        return expenseList?.stream()?.map { e -> ExpenseResponse(e) }?.toList()
    }

    fun createReview(expenseId: Long, createReviewRequest: CreateReviewRequest) {
        val review = reviewRepository.findByEmojiName(createReviewRequest.reviewType) ?: throw CustomException(ErrorCode.ENTITY_NOT_FOUND, "review")
        val expense =
            expenseRepository.findById(expenseId).orElseThrow { CustomException(ErrorCode.ENTITY_NOT_FOUND, "expense") }

        expenseReviewRepository.save(createReviewRequest.from(review, expense))
    }

    fun getAllReviewsByExpenseId(expenseId: Long): List<Review> {
        return expenseReviewRepository.getAllReviewsByExpense(expenseId)
    }
}
