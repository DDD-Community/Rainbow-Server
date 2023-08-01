package com.rainbow.server.rest.dto.expense

import java.time.LocalDate

data class ExpenseRequest(
    var amount:Int,
    var date: LocalDate,
    val categoryName:String,
    val categoryStatus:Boolean,
    val comment:String,
    val content:String

) {

}


data class CustomCategoryRequest(
    val name:String,
    val status:Boolean,
    val imagePath:String
)

// TODO: naming 괜찮은지 체크 부탁드립니다.
data class CommentRequest(
    var comment: String,
)
