//package com.rainbow.server.domain.expense.entity
//
//import com.rainbow.server.domain.BaseEntity
//import com.rainbow.server.domain.member.entity.Member
//import javax.persistence.*
//
//@Entity
//@Table(name="Goal")
//@Deprecated("다른 goal 로 바꿈")
//class Goal(
//    id: Long,
//    member: Member,
//    cost: Int,
//    yearMonth: String, //TODO: String으로 가져갈 것인지?
//    percentage: Float
//): BaseEntity() {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    var id: Long = id
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    val member: Member = member
//
//    @Column(nullable = false)
//    val cost: Int = cost
//
//    @Column(nullable = false, name = "year_month")
//    val yearMonth: String = yearMonth
//
//    @Column(nullable = false)
//    val percentage: Float = percentage
//}
