package com.rainbow.server.domain.goal.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDate
import javax.persistence.*


@Entity
@Table(name = "member")
class Goal(
    var cost:Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,
    val yearMonth: LocalDate
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var percentage: Double=0.0


    fun updateCost(cost:Long){
        this.cost=cost
    }

}
