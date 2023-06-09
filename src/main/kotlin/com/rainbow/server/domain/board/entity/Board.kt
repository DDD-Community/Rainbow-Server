package com.rainbow.server.domain.board.entity

import com.rainbow.server.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/***
 * 예시 Entity
 * */
@Entity
class Board(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long?=null,
    var writer:String,
    var title:String
):BaseEntity()



