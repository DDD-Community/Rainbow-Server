package com.rainbow.server.domain.goal.repository

import com.rainbow.server.domain.goal.entity.Goal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/***
 * 예시 Repository
 * */
@Repository
interface GoalRepository: JpaRepository<Goal, Long> {
}
