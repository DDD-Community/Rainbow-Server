package com.rainbow.server.domain.goal.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(name = "goal")
class Goal(
    var cost:Int,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    val time: LocalDate,

) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var paidAmount: Int=0

    var savedCost:Int=0


    fun updateCost(cost:Int){
        this.cost=cost
    }

}
