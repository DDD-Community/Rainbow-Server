package com.rainbow.server.service

import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.repository.CategoryRepository
import com.rainbow.server.domain.expense.repository.CustomCategoryRepository
import com.rainbow.server.domain.expense.repository.ExpenseRepository
import com.rainbow.server.domain.goal.repository.GoalRepository
import com.rainbow.server.rest.dto.expense.CustomCategoryRequest
import com.rainbow.server.rest.dto.expense.ExpenseRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class ExpenseService(
    private val expenseRepository: ExpenseRepository,
    private val goalRepository: GoalRepository,
    private val categoryRepository: CategoryRepository,
    private val customCategoryRepository: CustomCategoryRepository,
    private val memberService: MemberService
) {


    //TODO: 이 method는 goal 이랑 엮인게 있어서 일단 제가 만들겠습니다.
    @Transactional

    fun createExpense(expenseRequest: ExpenseRequest) {
        val currentMember = memberService.getCurrentLoginMember()
        val month = expenseRequest.date.monthValue
        val year = expenseRequest.date.year
        val goalDate = LocalDate.of(year, month, 1)
        val goal = goalRepository.findByMemberAndTime(currentMember.memberId, goalDate)
        val customCategory =
            customCategoryRepository.findByNameAndMember(expenseRequest.categoryName, currentMember).let {
               val category=categoryRepository.findByName(expenseRequest.categoryName)
                customCategoryRepository.save(
                    CustomCategory(
                        name = expenseRequest.categoryName,
                        status = expenseRequest.categoryStatus,
                        member = currentMember,
                        category = category,
                        imagePath = category.imagePath
                    )
                )
            }


        expenseRepository.save(
            Expense(
                goal = goal,
                amount = expenseRequest.amount,
                member = currentMember,
                date = expenseRequest.date,
                comment =expenseRequest.comment,
                content = expenseRequest.content,
                customCategory = customCategory
            )
        )

    }


    @Transactional
    fun createCustomCategory(customCategoryRequest: CustomCategoryRequest){
        val currentMember = memberService.getCurrentLoginMember()
        customCategoryRepository.save(CustomCategory(name = customCategoryRequest.name, status =customCategoryRequest.status, member = currentMember, category = null, imagePath =  customCategoryRequest.imagePath))
    }

}