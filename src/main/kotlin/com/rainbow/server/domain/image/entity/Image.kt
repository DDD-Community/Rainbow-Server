package com.rainbow.server.domain.image.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.expense.entity.Expense
import javax.persistence.*

@Entity
class Image(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="expenseId")
    val expense: Expense,
):BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0L

}