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
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name ="customCategoryId" )
    val customCategory: CustomCategory?,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "goalId")
    val goal: Goal,
    // TODO: length 몇으로 설정할 것인지
    // TODO: 지출평은 주 단위?
    @Column() var comment: String? = null,
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

    fun updateComment(comment: String?): Expense {
        this.comment = comment
        return this
    }

}

// TODO: 코드 리뷰 후 삭제 예정
//@Entity
//@Table(name = "Expense_Image")
//class ExpenseImage(
//    image: Image,
//    expense: Expense,
//): BaseEntity() {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var expenseImageId: Long = 0L
//
//    @OneToOne(fetch = FetchType.LAZY)
//    val image: Image = image
//
//}

@Entity
@Table(name="Category")
class Category(
    @Column(nullable = false) val name: String,
    imagePath:String
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long = 0L

    var imagePath:String = imagePath

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val customCategoryMutableList:MutableList<CustomCategory> = mutableListOf()
    val customCategoryList:List<CustomCategory> get()=customCategoryMutableList.toList()
}

@Entity
@Table(name="CustomCategory")
class CustomCategory(
    name: String,
    status:Boolean,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="categoryId")
    val category: Category?,
    imagePath: String
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val customCategoryId: Long = 0L

    @Column(nullable = false)
    val name: String = name

    @Column(nullable = false)
    val status: Boolean = status

    var imagePath:String=imagePath

    @OneToMany(mappedBy = "customCategory", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val expenseMutableList:MutableList<Expense> = mutableListOf()
    val expenseList:List<Expense> get()=expenseMutableList.toList()



}
