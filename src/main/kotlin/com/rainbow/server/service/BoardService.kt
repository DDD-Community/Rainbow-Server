package com.rainbow.server.service

import com.rainbow.server.domain.board.entity.Board
import com.rainbow.server.rest.dto.BoardRequest
import com.rainbow.server.domain.board.repository.BoardRepository
import com.rainbow.server.rest.dto.BoardResponse
import org.springframework.stereotype.Service

/***
 * 예시 Service
 * */
@Service
class BoardService(val boardRepository: BoardRepository) {

    fun save(boardRequest: BoardRequest): Board {
        return boardRepository.save(boardRequest.of(boardRequest))
    }

    fun findById(id:Long):BoardResponse{
        return BoardResponse.from(boardRepository.findById(id).get())
    }

    fun findAll():List<BoardResponse>{
        val boardList=boardRepository.findAll()
        return boardList.map { board->BoardResponse(board.id, board.writer, board.title) }
    }

    fun update(boardRequest: BoardRequest):BoardResponse{
       val board= boardRepository.findById(boardRequest.id).get()
        board.title=boardRequest.title
        return BoardResponse.from(boardRepository.save(board))
    }

    fun delete(id:Long){
        boardRepository.deleteById(id)
    }
}