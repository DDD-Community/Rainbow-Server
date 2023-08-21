package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.expense.CreateReviewRequest
import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
import com.rainbow.server.rest.dto.expense.CustomCategoryResponse
import com.rainbow.server.rest.dto.expense.DailyCharacter
import com.rainbow.server.rest.dto.expense.DailyExpenseResponse
import com.rainbow.server.rest.dto.expense.ExpenseRequest
import com.rainbow.server.rest.dto.expense.ExpenseResponse
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
import org.springframework.web.bind.annotation.RequestParam
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

    @PutMapping("/custom-category/{id}")
    fun updateCustomCategory(@PathVariable(name = "id")id: Long, @RequestBody customCategoryRequest: CustomCategoryRequest) {
        expenseService.updateCustomCategory(id, customCategoryRequest)
    }

    @GetMapping("/{date}")
    fun getDailyExpense(
        @PathVariable
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate,
    ): CommonResponse<DailyExpenseResponse> {
        return success(expenseService.createOrReadDailyExpense(date))
    }

    @GetMapping("/category/count")
    fun isUnderMaxCustomCategoryCount(): Boolean {
        return expenseService.countCustomCategory()
    }

    @GetMapping("/category/{id}")
    fun getCategoryInfo(@PathVariable(name = "id")id: Long): CommonResponse<CustomCategoryResponse> {
        return success(expenseService.getCustomCategory(id))
    }

    @PutMapping("/{expenseId}")
    fun modifyExpense(@PathVariable expenseId: Long, @RequestBody updateExpenseRequest: UpdateExpenseRequest) {
        expenseService.modifyExpense(updateExpenseRequest)
    }

    @PutMapping("/{dailyId}/character")
    fun updateDailyCharacterAndComment(
        @PathVariable dailyId: Long,
        @RequestBody updateDailyExpenseRequest: UpdateDailyExpenseRequest,
    ) {
        expenseService.updateDailyCharacterAndComment(dailyId, updateDailyExpenseRequest)
    }

    @GetMapping("/{date}/characters")
    fun getAllDaysCharacters(
        @PathVariable
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate,
    ): CommonResponse<List<DailyCharacter?>> {
        return success(expenseService.getAllDaysCharacters(date))
    }

    @GetMapping
    fun getAllExpensesByContent(
        @RequestParam(name = "content") content: String,
        @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        date: LocalDate,
    ): CommonResponse<List<ExpenseResponse>?> {
        return success(expenseService.getAllExpensesByContent(date, content))
    }

    @PostMapping("/reviews")
    fun createReview(@RequestBody createReviewRequest: CreateReviewRequest) {
        expenseService.createReview(createReviewRequest)
    }
}
