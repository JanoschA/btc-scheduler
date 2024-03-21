package com.btc_scheduler.controller

import com.btc_scheduler.mapper.ScheduleExtensions.toResponseSchedule
import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.ResponseSchedule
import com.btc_scheduler.service.SchedulerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation

@RestController
@RequestMapping("/schedules")
@Api(value = "Scheduler Controller", description = "Operations pertaining to schedules in Scheduler Application")
class SchedulerController(private val schedulerService: SchedulerService) {

    @PostMapping
    @ApiOperation(value = "Create a new schedule", response = ResponseEntity::class)
    fun createSchedule(@RequestBody schedule: RequestSchedule): ResponseEntity<ResponseSchedule> {
        val responseSchedule = schedulerService.createSchedule(schedule)
        return ResponseEntity.ok(responseSchedule)
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete a schedule", response = ResponseEntity::class)
    fun deleteSchedule(@PathVariable id: String): ResponseEntity<String> {
        schedulerService.deleteSchedule(id)
        return ResponseEntity.ok("Schedule deleted successfully")
    }

    @GetMapping
    @ApiOperation(value = "View a list of all schedules", response = ResponseEntity::class)
    fun listAllSchedules(): ResponseEntity<List<ResponseSchedule>> {
        val schedules = schedulerService.listAllSchedules()
        return ResponseEntity.ok(schedules.toResponseSchedule())
    }
}