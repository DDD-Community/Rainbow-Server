package com.rainbow.server.rest.dto

import com.rainbow.server.domain.board.entity.Board
import com.rainbow.server.domain.member.Member
import java.time.ZonedDateTime
import java.util.*

/***
 * 예시 Response
 * */

data class BoardResponse(
    var id:Long?,
    var writer: Long,
    var title:String,
    var content:String,
    var createdAt: ZonedDateTime,
    var updatedAt: ZonedDateTime,

    ) {

    companion object {
        fun from(board: Board): BoardResponse {
            return BoardResponse(
                id = board.id,
                writer = board.writer,
                title = board.title,
                content = board.content,
                createdAt =board.createdAt,
                updatedAt = board.updatedAt
            )
        }
    }
}






