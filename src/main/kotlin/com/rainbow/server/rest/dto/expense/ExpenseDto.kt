package com.rainbow.server.rest.dto.expense

import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.expense.entity.DailyExpense
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.entity.ExpenseReview
import com.rainbow.server.domain.expense.entity.Review
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.image.entity.Image
import java.time.LocalDate
import org.springframework.web.multipart.MultipartFile

data class ExpenseRequest(
    var amount: Int,
    var date: LocalDate,
    val categoryName: String,
    val categoryStatus: Boolean,
    val comment: String,
    val content: String,
    val dailyCharacter: String,
    val files: List<MultipartFile>?,
) {
    fun toCustom(currentMember: Member): CustomCategory {
        return CustomCategory(
            name = this.categoryName,
            status = this.categoryStatus,
            member = currentMember,
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
) {
    fun from(review: Review, expense: Expense): ExpenseReview {
        return ExpenseReview(
            review = review,
            expense = expense,
        )
    }
}
