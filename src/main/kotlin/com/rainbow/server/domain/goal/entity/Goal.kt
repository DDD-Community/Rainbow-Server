package com.rainbow.server.domain.goal.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
class Goal(
    var cost: Int,
    var savedCost: Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    val time: LocalDate

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val goalId: Long = 0

    var paidAmount: Int = 0

//    @OneToMany(mappedBy = "goal", cascade = [CascadeType.ALL], orphanRemoval = true)
//    protected val dailyExpenseMutableList: MutableList<DailyExpense> = mutableListOf()
//    val dailyExpenseList: List<DailyExpense> get() = dailyExpenseMutableList.toList()

    fun updateCost(cost: Int) {
        this.cost = cost
    }

    fun updatePaidAmountAndSavedCost(amount: Int) {
        this.paidAmount += amount
        this.savedCost = cost - paidAmount
    }

    fun modifyPaidAmountAndSavedCost(amount: Int, newAmount: Int) {
        this.paidAmount -= (amount - newAmount)
        savedCost = cost - paidAmount
    }
}
