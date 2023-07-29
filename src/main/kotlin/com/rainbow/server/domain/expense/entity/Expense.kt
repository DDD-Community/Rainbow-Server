package com.rainbow.server.domain.expense.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.image.entity.Image
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import javax.persistence.*
import com.rainbow.server.domain.goal.entity.Goal

@Entity
@Table(name="Expense")
class Expense(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "goal_id")
    val goal: Goal,
    comment: String? = null,
    date: LocalDate,
    amount: Int,
    content: String

): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val expenseId: Long = 0L

    @Column() // TODO: length 몇으로 설정할 것인지
    val comment: String? = comment

    @Column(nullable = false)
    val date: LocalDate = date

    @Column(nullable = false)
    val amount: Int = amount

    @Column(nullable = false)
    val content: String = content

    @OneToMany(mappedBy = "expense", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val imageMutableList:MutableList<Image> = mutableListOf()
    val imageList:List<Image> get()=imageMutableList.toList()


    // TODO: ManyToMany 제거 후 직접 연결
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//        name = "custom_category",
//        joinColumns = [JoinColumn(name = "expense_id")],
//        inverseJoinColumns = [JoinColumn(name = "category_id")]
//    )
//    val categories: Set<Category> = HashSet()

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//        name = ""
//    )
}

@Entity
@Table(name = "Expense_Image")
class ExpenseImage(
    image: Image,
    expense: Expense,
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var expenseImageId: Long = 0L

    @OneToOne(fetch = FetchType.LAZY)
    val image: Image = image

}

@Entity
@Table(name="Category")
class Category(
    id: Long,
    name: String,
    status: Boolean = true,
    member: Member
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = id

    @Column(nullable = false)
    val name: String = name

    @Column(nullable = false)
    val status: Boolean = status

    @ManyToOne(fetch = FetchType.LAZY)
    val member: Member = member
}

@Entity
@Table(name="CustomCategory")
class CustomCategory(
    id: Long,
    name: String,
    status: Boolean = true,
    member: Member
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = id

    @Column(nullable = false)
    val name: String = name

    @Column(nullable = false)
    val status: Boolean = status

    @ManyToOne(fetch = FetchType.LAZY)
    val member: Member = member
}
