package com.rainbow.server.service

import com.rainbow.server.domain.board.entity.Board
import com.rainbow.server.domain.board.entity.BoardImage
import com.rainbow.server.rest.dto.BoardRequest
import com.rainbow.server.domain.board.repository.BoardRepository
import com.rainbow.server.domain.board.repository.BoardImageRepository
import com.rainbow.server.domain.member.Member
import com.rainbow.server.rest.dto.BoardResponse
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.transaction.annotation.Transactional
import java.io.File
import java.util.UUID

/***
 * 예시 Service
 * */
@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val boardImageRepository: BoardImageRepository) {

    val filePath: String = System.getProperty("user.dir")!! // 임시 경로(추후 S3 경로로 변경)
    val uploadDir = File(filePath).parent + "/resources"

    @Transactional
    fun saveBoard(id: Long?, writer: Member, title: String, content: String, file: MultipartFile?) {
        val board = Board(id, writer, title, content)
        boardRepository.save(board)

        val originalFilename: String? = file?.originalFilename
        val saveFileName = getSaveFileName(originalFilename)

        if (file != null && originalFilename != null) {
            file.transferTo(File(uploadDir + saveFileName))
            val boardImage = BoardImage(originalFilename, saveFileName, board)
            boardImageRepository.save(boardImage)
        }
    }

    private fun getSaveFileName(originalFilename: String?): String {
        val extPosIndex: Int? = originalFilename?.lastIndexOf(".")
        val ext = originalFilename?.substring(extPosIndex?.plus(1) as Int)

        return UUID.randomUUID().toString() + "." + ext
    }

    fun save(boardRequest: BoardRequest): Board {
        return boardRepository.save(boardRequest.of(boardRequest))
    }

    fun findById(id:Long):BoardResponse{
        return BoardResponse.from(boardRepository.findById(id).get())
    }

    fun findAll():List<BoardResponse>{
        val boardList=boardRepository.findAll()
        return boardList.map { board->BoardResponse(board.id, board.writer, board.title, board.content, board.createdAt,board.updatedAt) }
    }

    fun update(boardRequest: BoardRequest):BoardResponse{
        val board = boardRepository.findById(boardRequest.id).get()
        board.title=boardRequest.title
        return BoardResponse.from(boardRepository.save(board))
    }

    fun delete(id:Long){
        boardRepository.deleteById(id)
    }
}