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
import com.rainbow.server.domain.image.entity.Image
import com.rainbow.server.exception.CustomException
import com.rainbow.server.exception.ErrorCode
import com.rainbow.server.rest.dto.expense.CreateReviewRequest
import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
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
import kotlin.collections.emptyList

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val dailyExpenseRepository: DailyExpenseRepository,
    private val goalRepository: GoalRepository,
    private val customCategoryRepository: CustomCategoryRepository,
    private val memberService: MemberService,
    private val imageService: ImageService,
    private val reviewRepository: ReviewRepository,
    private val expenseReviewRepository: ExpenseReviewRepository,
) {

    private val maxCategorySize: Int = 30

    @Transactional
    fun createExpense(expenseRequest: ExpenseRequest) {
        val currentMember = memberService.getCurrentLoginMember()
        val month = expenseRequest.date.monthValue
        val year = expenseRequest.date.year
        val goalDate = LocalDate.of(year, month, 1)
        val goal = goalRepository.findByMemberAndTime(currentMember.memberId, goalDate)
        goal.updatePaidAmountAndSavedCost(expenseRequest.amount)
        val customCategory =
            customCategoryRepository.findByNameAndMember(expenseRequest.categoryName, currentMember) ?: run {
                val newCustomCategory = customCategoryRepository.save(expenseRequest.toCustom(currentMember))
                newCustomCategory
            }

        val dailyExpense = dailyExpenseRepository.findByDateAndMember(expenseRequest.date, currentMember) ?: run {
            val newDailyExpense = DailyExpense(
                member = currentMember,
                goal = goal,
                comment = expenseRequest.comment,
                date = expenseRequest.date,
                dailyCharacter = expenseRequest.dailyCharacter,
            )
            newDailyExpense
        }

        val expense = Expense(
            amount = expenseRequest.amount,
            content = expenseRequest.content,
            customCategory = customCategory,
            dailyExpense = dailyExpense,
        )

        expenseRequest.files?.let {
            imageService.addImages(it, expense) // TODO: for문 여기로 옮기고 addImage 함수 다시 작성하기
        }

        dailyExpense.addExpense(expense)
        customCategory.addExpenseList(expense)
        dailyExpenseRepository.save(dailyExpense)
    }

    @Transactional
    fun createCustomCategory(customCategoryRequest: CustomCategoryRequest) {
        val currentMember = memberService.getCurrentLoginMember()
        customCategoryRepository.save(customCategoryRequest.to(currentMember))
    }

    fun countCustomCategory(): Boolean {
        val currentMember = memberService.getCurrentLoginMember()
        return currentMember.customCategoryList.size < maxCategorySize
    }

    fun getDailyExpense(date: LocalDate): DailyExpenseResponse {
        val currentMember = memberService.getCurrentLoginMember()
        val dailyExpense = dailyExpenseRepository.findByDateAndMember(date, currentMember)
        return DailyExpenseResponse(dailyExpense)
    }

    fun modifyExpense(expenseRequest: UpdateExpenseRequest) {
        val expense = expenseRepository.findById(expenseRequest.id).orElseThrow()
        val goal = expense.dailyExpense.goal
        goal.modifyPaidAmountAndSavedCost(expense.amount, expenseRequest.amount)
        expense.modifyExpense(expenseRequest.amount, expenseRequest.content)
        goalRepository.save(goal)
        expenseRepository.save(expense)
    }

    fun updateDailyCharacter(id: Long, updateDailyExpenseRequest: UpdateDailyExpenseRequest) {
        val dailyExpense = dailyExpenseRepository.findById(id).orElseThrow()
        updateDailyExpenseRequest.dailyCharacter?.let { dailyExpense.updateCharacter(it) }
        dailyExpenseRepository.save(dailyExpense)
    }

    fun updateDailyComment(id: Long, updateDailyExpenseRequest: UpdateDailyExpenseRequest) {
        val dailyExpense = dailyExpenseRepository.findById(id).orElseThrow()
        updateDailyExpenseRequest.comment?.let { dailyExpense.updateComment(it) }
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

    fun getAllExpensesByContent(content: String): List<ExpenseResponse>? {
        val currentMember = memberService.getCurrentLoginMember()
        val expenseList = expenseRepository.getAllExpensesByContent(content, currentMember)
        return expenseList?.stream()?.map { e -> ExpenseResponse(e) }?.toList()
    }

    fun createReview(expenseId: Long, createReviewRequest: CreateReviewRequest) {
        val review = reviewRepository.findById(createReviewRequest.reviewId).orElseThrow { CustomException(ErrorCode.ENTITY_NOT_FOUND, "review") }
        val expense = expenseRepository.findById(expenseId).orElseThrow { CustomException(ErrorCode.ENTITY_NOT_FOUND, "expense") }

        expenseReviewRepository.save(createReviewRequest.from(review, expense))
    }

    fun getAllReviewsByExpenseId(expenseId: Long): List<Review> {
        return expenseReviewRepository.getAllReviewsByExpense(expenseId)
    }
}
