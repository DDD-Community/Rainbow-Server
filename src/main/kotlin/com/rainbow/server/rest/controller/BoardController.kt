package com.rainbow.server.rest.controller

import com.rainbow.server.rest.dto.BoardRequest
import com.rainbow.server.service.BoardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/board")
class BoardController(val boardService: BoardService) {

    /**
     * 예시 CRUD
     * **/

    @PostMapping("/save")
    fun saveBoard(@RequestBody boardRequest: BoardRequest): ResponseEntity<Any> {
        val save=boardService.save(boardRequest)
        return ResponseEntity.ok().body(save)
    }

    @GetMapping("/find")
    fun findBoard(id:Long): ResponseEntity<Any> {
        return ResponseEntity.ok().body(boardService.findById(id))
    }

    @GetMapping("/findAll")
    fun findAllBoard(): ResponseEntity<Any> {
        return ResponseEntity.ok().body(boardService.findAll())
    }

    @PutMapping("/update")
    fun updateBoard(@RequestBody boardRequest: BoardRequest): ResponseEntity<Any> {
        val save=boardService.update(boardRequest)
        return ResponseEntity.ok().body(save)
    }

    @DeleteMapping("/delete")
    fun deleteBoard(id:Long): ResponseEntity<Any> {
        boardService.delete(id)
        return ResponseEntity.ok().body(id)
    }

}