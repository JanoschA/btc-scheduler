package com.btc_scheduler.scheduler

import com.btc_scheduler.repository.SchedulerRepository
import com.btc_scheduler.service.BrokerService

class TaskRunner(
    private val scheduleId: String,
    private val schedulerRepository: SchedulerRepository,
    private val brokerService: BrokerService
) : Runnable {
    override fun run() {

        val schedule = schedulerRepository.findById(scheduleId)

        schedule.ifPresent {
            if (it.active.not()) {
                println("Task is not active for schedule: $scheduleId")
                return@ifPresent
            }

            brokerService.buy(it.amount)
        }
    }
}