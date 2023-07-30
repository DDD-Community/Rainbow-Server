package com.rainbow.server.rest.controller

import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
import com.rainbow.server.rest.dto.expense.ExpenseRequest
import com.rainbow.server.service.ExpenseService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}