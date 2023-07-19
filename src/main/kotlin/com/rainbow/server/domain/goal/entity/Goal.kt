package com.rainbow.server.domain.goal.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.member.entity.Member
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "goal")
class Goal(
    var cost:Long,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    val member: Member,
    val time: LocalDateTime
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    var percentage: Double=0.0


    fun updateCost(cost:Long){
        this.cost=cost
    }

}
