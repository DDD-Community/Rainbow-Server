package com.rainbow.server.domain.image.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.expense.entity.Expense
import java.util.*
import javax.persistence.*

@Entity
@Table(name="Image")
class Image(
    id: Long? = null,
    originalFileName: String?,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="expenseId")
    val expense: Expense,
    saveFileName: String
):BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = id

    @Column(nullable = false)
    val originalFileName: String? = originalFileName

    @Column(nullable = false)
    val saveFileName: String = saveFileName

}