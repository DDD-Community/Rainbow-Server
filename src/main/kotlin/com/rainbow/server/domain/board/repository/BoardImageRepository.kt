package com.rainbow.server.domain.board.repository

import com.rainbow.server.domain.board.entity.BoardImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface BoardImageRepository: JpaRepository<BoardImage, Long> {
}
