package com.rainbow.server.domain.member.entity

import com.rainbow.server.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "member")
class Member (email:String,nickName:String,ageRange:String,gender:String
):BaseEntity(){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var email:String =email
    var nickName:String=nickName
    var ageRange:String=ageRange
    var gender:String=gender
}
