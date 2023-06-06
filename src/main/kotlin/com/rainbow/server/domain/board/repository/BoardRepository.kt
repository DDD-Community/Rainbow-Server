package com.rainbow.server.domain.board.repository

import com.rainbow.server.domain.board.entity.Board
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/***
 * 예시 Repository
 * */
@Repository
interface BoardRepository: JpaRepository<Board, Long> {
}
