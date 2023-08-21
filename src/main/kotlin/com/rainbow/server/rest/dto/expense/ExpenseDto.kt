package com.rainbow.server.rest.dto.expense

import com.rainbow.server.domain.expense.entity.*
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate

data class ExpenseRequest(
    var amount: Int,
    var date: LocalDate,
    val categoryName: String,
    val categoryStatus: Boolean,
    val comment: String,
    val content: String,
    val dailyCharacter: String,
) {
    fun toCustom(currentMember: Member, category: Category): CustomCategory {
        return CustomCategory(
            name = this.categoryName,
            status = this.categoryStatus,
            member = currentMember,
            category = category,
            customCategoryImage = category.categoryImage,
        )
    }
}

data class ExpenseResponse(
    var amount: Int?,
    val categoryName: String?,
    val categoryStatus: Boolean?,
    val content: String?,
    val expenseId: Long?,
) {
    constructor(expense: Expense?) : this(
        amount = expense?.amount,
        categoryName = expense?.customCategory?.name,
        categoryStatus = expense?.customCategory?.status,
        content = expense?.content,
        expenseId = expense?.expenseId,
    )
}

data class CustomCategoryRequest(
    val name: String,
    val status: Boolean,
    val customCategoryImage: String,
) {
    fun to(currentMember: Member): CustomCategory {
        return CustomCategory(
            name = this.name,
            status = this.status,
            member = currentMember,
            category = null,
            customCategoryImage = this.customCategoryImage,
        )
    }
}

data class UpdateExpenseRequest(
    val id: Long,
    var amount: Int,
    val content: String,
)

data class UpdateDailyExpenseRequest(
    val comment: String?,
    val dailyCharacter: String?,
)

data class DailyExpenseResponse(
    val comment: String?,
    val date: LocalDate?,
    val expenseList: List<ExpenseResponse>?,
) {
    constructor(dailyExpense: DailyExpense?) : this(
        comment = dailyExpense?.comment,
        date = dailyExpense?.date,
        expenseList = dailyExpense?.expenseList?.map { ExpenseResponse(it) },
    )
}

data class DailyCharacter(
    var character: String?,
    var date: LocalDate?,
) {
    constructor(expense: DailyExpense?) : this (
        character = expense?.dailyCharacter,
        date = expense?.date,
    )
}

data class CreateReviewRequest(
    var reviewId: Long,
    var expenseId: Long,
) {
    fun to(review: Review, expense: Expense): ExpenseReview {
        return ExpenseReview(
            review = review,
            expense = expense,
        )
    }
}
