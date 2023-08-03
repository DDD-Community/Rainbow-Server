package com.rainbow.server.domain.member.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Salary(
    val salaryRange: String,
    val idx: Int
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val salaryId: Long = 0L
}
