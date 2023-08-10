package com.rainbow.server.domain.expense.repository

import com.rainbow.server.domain.expense.entity.DailyExpense
import com.rainbow.server.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyExpenseRepository : JpaRepository<DailyExpense, Long> {
    fun findByDateAndMember(date: LocalDate, member: Member): DailyExpense?

    fun findAllByMemberAndDateBetween(member: Member, startDate: LocalDate, endDate: LocalDate): List<DailyExpense>?
}
