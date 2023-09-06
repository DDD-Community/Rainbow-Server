package com.rainbow.server.rest.dto.expense

import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.expense.entity.DailyExpense
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.entity.ExpenseReview
import com.rainbow.server.domain.expense.entity.Review
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import kotlin.streams.toList

data class ExpenseRequest(
    var amount: Int,
    var date: LocalDate,
    val categoryId: Long,
    val content: String,
    val dailyExpenseId: Long,
    val memo: String,
)

data class ExpenseResponse(
    var amount: Int?,
    val content: String?,
    val expenseId: Long?,
    val date: LocalDate?,
    val memo: String?,
    val imageList: List<String>?,
) {
    constructor(expense: Expense?) : this(
        amount = expense?.amount,
        content = expense?.content,
        expenseId = expense?.expenseId,
        date = expense?.dailyExpense?.date,
        memo = expense?.memo,
        imageList = expense?.imageList?.map { it.saveFileName },
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

data class CustomCategoryExpenseListResponse(
    val categoryId: Long,
    val expenseList: List<ExpenseResponse>?,
) {
    constructor(customCategory: CustomCategory) : this(
        categoryId = customCategory.customCategoryId,
        expenseList = customCategory.expenseList.stream().map { e -> ExpenseResponse(e) }.toList(),
    )
}

data class CustomCategoryResponse(
    val categoryId: Long,
    val name: String,
    val status: Boolean,
) {
    constructor(customCategory: CustomCategory) : this(
        categoryId = customCategory.customCategoryId,
        name = customCategory.name,
        status = customCategory.status,
    )
}

data class FriendsExpenseDto(
    val memberId: Long,
    val nickName: String,
    val expenseResponse: ExpenseResponse,
    val date: LocalDate,
    var isFriend: Boolean,
) {
    constructor(
        memberId: Long,
        nickName: String,
        expenseResponse: ExpenseResponse,
        date: LocalDate,
    ) : this(
        memberId = memberId,
        nickName = nickName,
        expenseResponse = expenseResponse,
        date = date,
        isFriend = true,
    )

    fun updateIsFriend(isFriend: Boolean) {
        this.isFriend = isFriend
    }
}

data class UpdateExpenseRequest(
    val id: Long,
    var amount: Int,
    val content: String,
    val memo: String,
)

data class UpdateDailyExpenseRequest(
    val comment: String,
    val dailyCharacter: String,
)

data class DailyExpenseRequest(
    val id: Long = 0L,
    val date: LocalDate,
)

data class DailyExpenseResponse(
    val dailyExpenseId: Long,
    val comment: String?,
    val date: LocalDate?,
    val dailyCharacter: String?,
    val expenseList: List<ExpenseResponse>?,
) {
    constructor(dailyExpense: DailyExpense) : this(
        dailyExpenseId = dailyExpense.dailyExpenseId,
        comment = dailyExpense.comment,
        date = dailyExpense.date,
        dailyCharacter = dailyExpense.dailyCharacter,
        expenseList = dailyExpense.expenseList.map { ExpenseResponse(it) },
    )
}

data class DailyCharacter(
    var character: String?,
    var date: LocalDate?,
) {
    constructor(expense: DailyExpense?) : this(
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
