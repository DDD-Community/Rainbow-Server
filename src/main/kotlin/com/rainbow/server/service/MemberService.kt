package com.rainbow.server.service

import com.rainbow.server.domain.member.Member
import com.rainbow.server.domain.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class MemberService(private val memberRepository: MemberRepository) {

    fun findById(id: Long): Member{
        return memberRepository.findById(id).get()
    }
}