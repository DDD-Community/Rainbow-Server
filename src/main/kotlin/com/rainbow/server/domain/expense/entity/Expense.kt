package com.rainbow.server.domain.expense.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.image.entity.Image
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import javax.persistence.*
import com.rainbow.server.domain.goal.entity.Goal

@Entity
@Table(name="daily_expense")
class DailyExpense(
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "memberId")
    val member: Member,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "goalId")
    val goal: Goal,
    @Column()
    val comment: String? = null,
    @Column(nullable = false)
    val date: LocalDate,
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val dailyExpenseId: Long = 0L


    @OneToMany(mappedBy = "dailyExpense", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val expenseMutableList:MutableList<Expense> = mutableListOf()
    val expenseList:List<Expense> get()=expenseMutableList.toList()

    fun addExpense(expense: Expense){
        expenseMutableList.add(expense)
    }


}

@Entity
@Table(name="expense")
class Expense(

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name ="customCategoryId" )
    val customCategory: CustomCategory,
    // TODO: length 몇으로 설정할 것인지

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name ="dailyExpenseId" )
    val dailyExpense: DailyExpense,
    amount: Int,
    content: String

): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val expenseId: Long = 0L

    @Column(nullable = false)
    var amount:Int=amount

    @Column(nullable = false)
    var content:String=content

    @OneToMany(mappedBy = "expense", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val imageMutableList:MutableList<Image> = mutableListOf()
    val imageList:List<Image> get()=imageMutableList.toList()

    fun modifyExpense(amount: Int,content:String){
        this.amount=amount
        this.content=content
    }

}

@Entity
@Table(name = "expense_image")
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
@Table(name="category")
class Category(
    name: String,
    imagePath:String
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long = 0L

    @Column(nullable = false)
    val name: String = name

    var imagePath:String=imagePath

    @OneToMany(mappedBy = "category", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val customCategoryMutableList:MutableList<CustomCategory> = mutableListOf()
    val customCategoryList:List<CustomCategory> get()=customCategoryMutableList.toList()


}

@Entity
@Table(name="custom_category")
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


    fun addExpenseList(expense: Expense){
        expenseMutableList.add(expense)
    }

}
