package com.rainbow.server.domain.expense.repository

import com.rainbow.server.domain.expense.entity.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository : JpaRepository<Expense, Long>
