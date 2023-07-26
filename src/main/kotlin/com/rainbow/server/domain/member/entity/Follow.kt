package com.rainbow.server.domain.member.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Follow( var fromMember:Long,
             var toMember:Long) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val followId:Long=0L

}