package com.rainbow.server.domain.member.repository

import com.rainbow.server.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/***
 * 예시 Repository
 * */
@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
}
