package com.rainbow.server.service

import com.rainbow.server.domain.expense.repository.ExpenseRepository
import org.springframework.stereotype.Service

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository) {


    //TODO: 이 method는 goal 이랑 엮인게 있어서 일단 제가 만들겠습니다.
    fun createExpense(){

    }
}