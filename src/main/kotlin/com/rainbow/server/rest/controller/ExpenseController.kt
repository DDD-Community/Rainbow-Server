package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
import com.rainbow.server.rest.dto.expense.DailyExpenseResponse
import com.rainbow.server.rest.dto.expense.ExpenseRequest
import com.rainbow.server.service.ExpenseService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/expense")
class ExpenseController(
    private val expenseService: ExpenseService
) {

    @PostMapping("/save")
    fun createExpense(@RequestBody expenseRequest: ExpenseRequest){
        expenseService.createExpense(expenseRequest)
    }

    @PostMapping("/create/customCategory")
    fun createCustomCategory(@RequestBody customCategoryRequest: CustomCategoryRequest){
        expenseService.createCustomCategory(customCategoryRequest)
    }

    @GetMapping("/getDailyExpense")
    fun getDailyExpense(@RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)date:LocalDate):CommonResponse<DailyExpenseResponse>{
        return success(expenseService.getDailyExpense(date))
    }
}