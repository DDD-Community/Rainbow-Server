package com.rainbow.server

import com.rainbow.server.domain.expense.entity.Category
import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.expense.entity.Expense
import com.rainbow.server.domain.expense.repository.CustomCategoryRepository
import com.rainbow.server.domain.expense.repository.ExpenseRepository
import com.rainbow.server.domain.goal.repository.GoalRepository
import com.rainbow.server.domain.member.entity.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import com.rainbow.server.rest.dto.expense.CommentRequest
import com.rainbow.server.service.ExpenseService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class CommentServiceTests(
    @Autowired private val expenseService: ExpenseService,
    @Autowired private val expenseRepository: ExpenseRepository,
    @Autowired private val customeCategory: Category,
    @Autowired private val customCategoryRepository: CustomCategoryRepository,
    @Autowired private val member: Member,
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val goalRepository: GoalRepository
) {

    private var expenseId: Long = 0L

    @BeforeEach
    fun setUp() {
        val newMember = Member(
            kaKaoId = 1000,
            email = "Test@test.test",
            gender = "M",
            birthDate = LocalDate.now(),
            password = "test1234",
            salary = 10000,
            nickName = "TestAccount"
        )

        memberRepository.save(newMember)

        val newCustomCategory = CustomCategory(
            name = "테스트 지출",
            status = true,
            member = memberRepository.findByEmail("Test@test.com")!!,
            category = null,
            imagePath = "testPath"
        )

        customCategoryRepository.save(newCustomCategory )

        val newExpense = Expense(
            customCategory = customCategoryRepository.findAll().firstOrNull(),
            member = memberRepository.findAll().first(),
            goal = goalRepository.findAll().first(),
            date = LocalDate.now(),
            amount = 1000,
            content = "테스트 지출"
        )
        var expense = expenseRepository.save(newExpense)

        expenseId = expense.expenseId
    }

    @Test
    fun testWriteComment() {
        var commentDto = CommentRequest("테스트 코드입니다.")
        var expense = expenseService.createComment(expenseId = expenseId, commentDto)

        assertEquals(expense.comment, commentDto.comment)
    }
}
