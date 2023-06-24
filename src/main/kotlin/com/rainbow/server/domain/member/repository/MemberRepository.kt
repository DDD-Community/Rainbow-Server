package com.rainbow.server.domain.member.repository

import com.rainbow.server.domain.board.entity.Board
import com.rainbow.server.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

/***
 * 예시 Repository
 * */
@Repository
interface MemberRepository: JpaRepository<Member, Long> {
    fun findByEmail(email: String): Member?
    override fun findById(id: Long): Member
}
