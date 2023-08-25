package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.expense.FriendsExpenseDto
import com.rainbow.server.service.MemberService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/feeds")
class FeedController(private val memberService: MemberService,) {

    @GetMapping("/friend-feeds")
    fun getFriendsFeeds(@RequestParam(required = false)lastId: Long?): CommonResponse<List<FriendsExpenseDto>?> {
        return success(memberService.getFriendsFeeds(lastId))
    }
}
