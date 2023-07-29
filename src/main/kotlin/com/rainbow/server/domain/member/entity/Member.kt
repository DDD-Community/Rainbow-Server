package com.rainbow.server.domain.member.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.goal.entity.Goal
import java.time.LocalDate
import javax.persistence.*

enum class Role { USER, ADMIN }

@Entity
@Table(name = "member")
class Member(
    val kaKaoId:Long,
   val email: String,
   val gender: String,
   val birthDate: LocalDate,
   val password:String,
   var salary: Int,
   var nickName: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val memberId: Long = 0L

    @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    protected val goalMutableList:MutableList<Goal> = mutableListOf()
    val goalList:List<Goal> get()=goalMutableList.toList()


    fun addGoalList(goal: Goal){
        goalMutableList.add(goal)
    }

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        protected set
}
