package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
import com.rainbow.server.rest.dto.expense.DailyExpenseResponse
import com.rainbow.server.rest.dto.expense.ExpenseRequest
import com.rainbow.server.rest.dto.expense.UpdateDailyExpenseRequest
import com.rainbow.server.rest.dto.expense.UpdateExpenseRequest
import com.rainbow.server.service.ExpenseService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val expenseService: ExpenseService,
) {

    @PostMapping
    fun createExpense(@RequestBody expenseRequest: ExpenseRequest) {
        expenseService.createExpense(expenseRequest)
    }

    @PostMapping("/custom-category")
    fun createCustomCategory(@RequestBody customCategoryRequest: CustomCategoryRequest) {
        expenseService.createCustomCategory(customCategoryRequest)
    }

    @GetMapping("/{date}")
    fun getDailyExpense(
        @PathVariable
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate,
    ): CommonResponse<DailyExpenseResponse> {
        return success(expenseService.getDailyExpense(date))
    }

    @GetMapping("/category/count")
    fun isUnderMaxCustomCategoryCount(): Boolean {
        return expenseService.countCustomCategory()
    }

    @PutMapping("/{expenseId}")
    fun modifyExpense(@PathVariable expenseId: Long, @RequestBody updateExpenseRequest: UpdateExpenseRequest) {
        expenseService.modifyExpense(updateExpenseRequest)
    }

    @PutMapping("/{dailyId}/character")
    fun updateDailyCharacter(@PathVariable dailyId: Long, @RequestBody updateDailyExpenseRequest: UpdateDailyExpenseRequest) {
        expenseService.updateDailyCharacter(dailyId, updateDailyExpenseRequest)
    }

    @PutMapping("/{dailyId}/comment")
    fun updateDailyComment(@PathVariable dailyId: Long, @RequestBody updateDailyExpenseRequest: UpdateDailyExpenseRequest) {
        expenseService.updateDailyComment(dailyId, updateDailyExpenseRequest)
    }
}
