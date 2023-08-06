package com.rainbow.server.domain.member.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.goal.entity.Goal
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

enum class Role { USER }

@Entity
class Member(
    val kaKaoId: Long,
    val email: String,
    val gender: String,
    val birthDate: LocalDate,
    val password: String,
    var salary: Int,
    var nickName: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long = 0L

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val goalMutableList: MutableList<Goal> = mutableListOf()
    val goalList: List<Goal> get() = goalMutableList.toList()

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val customCategoryMutableList: MutableList<CustomCategory> = mutableListOf()
    val customCategoryList: List<CustomCategory> get() = customCategoryMutableList.toList()

    fun addGoalList(goal: Goal) {
        goalMutableList.add(goal)
    }

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        protected set
}
