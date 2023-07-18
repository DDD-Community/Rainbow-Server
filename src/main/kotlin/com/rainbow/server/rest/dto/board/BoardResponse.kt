package com.rainbow.server.rest.dto.board

import com.rainbow.server.domain.board.entity.Board
import java.time.ZonedDateTime

/***
 * 예시 Response
 * */

data class BoardResponse (var id:Long?,
                          var writer:String,
                          var title:String,
                          var createdAt: ZonedDateTime,
                          var updatedAt: ZonedDateTime,

                          ) {

    companion object {
        fun from(board: Board): BoardResponse {
            return BoardResponse(
                id = board.id,
                writer = board.writer,
                title = board.title,
                createdAt=board.createdAt,
                updatedAt = board.updatedAt
            )
        }
    }
}






