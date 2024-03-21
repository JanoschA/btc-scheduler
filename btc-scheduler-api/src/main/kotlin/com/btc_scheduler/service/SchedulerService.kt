package com.btc_scheduler.service

import com.btc_scheduler.mapper.RequestScheduleExtensions.toSchedule
import com.btc_scheduler.mapper.ScheduleExtensions.toResponseSchedule
import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.ResponseSchedule
import com.btc_scheduler.model.Schedule
import com.btc_scheduler.repository.SchedulerRepository
import com.btc_scheduler.scheduler.DynamicTaskScheduler
import org.springframework.stereotype.Service

@Service
class SchedulerService(
    private val schedulerRepository: SchedulerRepository,
    private val dynamicTaskScheduler: DynamicTaskScheduler
) {

    fun createSchedule(requestSchedule: RequestSchedule): ResponseSchedule {
        val schedule = schedulerRepository.save(requestSchedule.toSchedule())

        if (schedule.active) {
            dynamicTaskScheduler.scheduleTask(schedule)
        }

        return schedule.toResponseSchedule()
    }

    fun deleteSchedule(id: String) {
        schedulerRepository.deleteById(id)
        dynamicTaskScheduler.cancelTask(id)
    }

    fun listAllSchedules(): List<Schedule> {
        return schedulerRepository.findAll()
    }
}