package com.rainbow.server.domain.expense.repository

import com.rainbow.server.domain.expense.entity.CustomCategory
import com.rainbow.server.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCategoryRepository : JpaRepository<CustomCategory, Long> {
    fun findByNameAndMember(name: String, member: Member): CustomCategory?
}
