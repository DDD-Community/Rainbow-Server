package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.goal.GoalRequestDto
import com.rainbow.server.rest.dto.goal.GoalResponseDto
import com.rainbow.server.service.GoalService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/goal")
class GoalController(private val goalService: GoalService) {

    @PostMapping("/createGoal")
    fun createGoal(@RequestBody goalRequestDto: GoalRequestDto){
        goalService.createGoal(goalRequestDto)
    }

    @PutMapping("/updateGoal")
    fun updateGoal(@RequestBody goalRequestDto: GoalRequestDto):CommonResponse<GoalResponseDto>{
        return success(goalService.updateGoal(goalRequestDto))
    }

    @GetMapping("/currentMonth")
    fun getCurrentMonth():CommonResponse<GoalResponseDto?>{
      return success(goalService.getCurrentMonth())
    }

    @GetMapping("/yearTest")
    fun getYearly():CommonResponse<Any>{
        return success(goalService.getYearlyGoals())
    }
}