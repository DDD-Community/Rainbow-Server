package com.rainbow.server.rest.dto

import com.rainbow.server.domain.board.entity.Board
/***
 * 예시 Request
 * */
data class BoardRequest( var id:Long=0,
                         var writer:String,
                         var title:String) {

    fun of(boardRequest: BoardRequest): Board {
        return Board(
            id=boardRequest.id,
            writer=boardRequest.writer,
            title=boardRequest.title
        )
    }
}