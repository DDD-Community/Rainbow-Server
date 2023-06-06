package com.rainbow.server.rest.dto

import com.rainbow.server.domain.board.entity.Board

/***
 * 예시 Response
 * */

data class BoardResponse ( var id:Long?,
                           var writer:String,
                           var title:String,
     ) {

    companion object {
        fun from(board: Board): BoardResponse {
            return BoardResponse(
                id = board.id,
                writer = board.writer,
                title = board.title
            )
        }
    }
}






