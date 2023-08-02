package com.rainbow.server.rest.controller

import com.rainbow.server.common.CommonResponse
import com.rainbow.server.common.success
import com.rainbow.server.rest.dto.goal.GoalRequestDto
import com.rainbow.server.rest.dto.goal.GoalResponseDto
import com.rainbow.server.service.GoalService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/goals")
class GoalController(private val goalService: GoalService) {

    @PostMapping
    fun createGoal(@RequestBody goalRequestDto: GoalRequestDto){
        goalService.createGoal(goalRequestDto)
    }

    @PutMapping("/{id}")
    fun updateGoal(@PathVariable id:Long,@RequestBody goalRequestDto: GoalRequestDto):CommonResponse<GoalResponseDto>{
        return success(goalService.updateGoal(id,goalRequestDto))
    }

    @GetMapping("/{date}")
    fun getCurrentMonth(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) date:LocalDate):CommonResponse<GoalResponseDto?>{
      return success(goalService.getCurrentMonth(date))
    }

    /*
    * 연도별 데이터 조회 테스트 메소드
    * */
    @GetMapping("/yearTest")
    fun getYearly():CommonResponse<Any>{
        return success(goalService.getYearlyGoals())
    }
}