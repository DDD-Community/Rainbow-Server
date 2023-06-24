package com.rainbow.server.rest.dto

import com.rainbow.server.domain.board.entity.Board
import com.rainbow.server.domain.member.Member
import com.rainbow.server.domain.member.repository.MemberRepository

/***
 * 예시 Request
 * */
data class BoardRequest( var id:Long=0,
                         var writer:Member,
                         var title:String,
                         var content:String) {

    fun of(boardRequest: BoardRequest): Board {
        return Board(
            id =boardRequest.id,
            writer =boardRequest.writer,
            title =boardRequest.title,
            content =boardRequest.content
        )
    }
}