package com.rainbow.server.domain.member.entity

import com.rainbow.server.domain.BaseEntity
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
   var salary: Double,
   var nickName: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Enumerated(EnumType.STRING)
    var role: Role = Role.USER
        protected set
}
