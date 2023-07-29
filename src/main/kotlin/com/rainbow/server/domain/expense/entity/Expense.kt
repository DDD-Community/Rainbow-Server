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
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "goalId")
    val goal: Goal,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name ="customCategoryId" )
    val customCategory: CustomCategory,
    // TODO: length 몇으로 설정할 것인지
    @Column() val comment: String? = null,
    @Column(nullable = false) val date: LocalDate,
    @Column(nullable = false) val amount: Int,
    @Column(nullable = false) val content: String

): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val expenseId: Long = 0L

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
    name: String,
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long = 0L

    @Column(nullable = false)
    val name: String = name

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val customCategoryMutableList:MutableList<CustomCategory> = mutableListOf()
    val customCategoryList:List<CustomCategory> get()=customCategoryMutableList.toList()


}

@Entity
@Table(name="CustomCategory")
class CustomCategory(
    name: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoryId")
    val category: Category
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val customCategoryId: Long = 0L

    @Column(nullable = false)
    val name: String = name


    @OneToMany(mappedBy = "customCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val expenseMutableList:MutableList<Expense> = mutableListOf()
    val expenseList:List<Expense> get()=expenseMutableList.toList()



}
