package com.rainbow.server.rest.dto.expense

import com.rainbow.server.domain.expense.entity.Category
import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.expense.entity.DailyExpense
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate

data class ExpenseRequest(
    var amount: Int,
    var date: LocalDate,
    val categoryName: String,
    val categoryStatus: Boolean,
    val comment: String,
    val content: String

) {
    fun toCustom(currentMember: Member, category: Category): CustomCategory {
        return CustomCategory(
            name = this.categoryName,
            status = this.categoryStatus,
            member = currentMember,
            category = category,
            imagePath = category.imagePath
        )
    }
}

data class ExpenseResponse(
    var amount: Int?,
    val categoryName: String?,
    val categoryStatus: Boolean?,
    val content: String?
) {
    constructor(expense: Expense?) : this(
        amount = expense?.amount,
        categoryName = expense?.customCategory?.name,
        categoryStatus = expense?.customCategory?.status,
        content = expense?.content
    )
}

data class CustomCategoryRequest(
    val name: String,
    val status: Boolean,
    val imagePath: String
) {
    fun to(currentMember: Member): CustomCategory {
        return CustomCategory(
            name = this.name,
            status = this.status,
            member = currentMember,
            category = null,
            imagePath = this.imagePath
        )
    }
}

data class UpdateExpenseRequest(
    val id: Long,
    var amount: Int,
    val content: String
)

data class DailyExpenseResponse(
    val comment: String?,
    val date: LocalDate?,
    val expenseList: List<ExpenseResponse>?
) {
    constructor(dailyExpense: DailyExpense?) : this(
        comment = dailyExpense?.comment,
        date = dailyExpense?.date,
        expenseList = dailyExpense?.expenseList?.map { ExpenseResponse(it) }
    )
}
