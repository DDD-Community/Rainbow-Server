package com.rainbow.server.domain.board.entity

import com.rainbow.server.domain.BaseEntity
import com.rainbow.server.domain.member.entity.Member
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.FetchType
import javax.persistence.ManyToOne

/***
 * 예시 Entity
 * */

@Entity
class Board(
    id: Long? = null,
    writer: Member,
    title: String,
    content: String,
):BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = id

    @Column(nullable = false)
    var title: String = title

    @Column(nullable = false)
    var content: String = content

    @ManyToOne(fetch = FetchType.LAZY)
    val writer: Member = writer
}
