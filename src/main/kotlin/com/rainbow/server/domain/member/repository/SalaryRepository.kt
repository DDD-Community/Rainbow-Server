package com.rainbow.server.domain.member.repository

import com.rainbow.server.domain.member.entity.Salary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SalaryRepository : JpaRepository<Salary, Long>
