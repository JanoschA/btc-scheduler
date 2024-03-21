package com.btc_scheduler.scheduler

import com.btc_scheduler.model.Schedule
import com.btc_scheduler.repository.SchedulerRepository
import com.btc_scheduler.service.BrokerService
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import org.springframework.scheduling.support.CronTrigger
import java.util.concurrent.ScheduledFuture

@Configuration
@EnableScheduling
open class DynamicTaskScheduler(
    private val schedulerRepository: SchedulerRepository,
    private val taskScheduler: TaskScheduler,
    private val brokerService: BrokerService
) : SchedulingConfigurer {

    private val scheduledTasks: MutableMap<String, ScheduledFuture<*>> = mutableMapOf()

    @PostConstruct
    fun init() {
        val schedules = schedulerRepository.findAll()

        schedules.forEach { schedule ->
            if (schedule.active) {
                scheduleTask(schedule)
            }
        }
    }

    fun scheduleTask(schedule: Schedule) {
        val task = TaskRunner(schedule.id!!, schedulerRepository, brokerService)
        val scheduledTask = taskScheduler.schedule(task, CronTrigger(schedule.cron))
        scheduledTasks[schedule.id!!] = scheduledTask!!
    }

    fun cancelTask(id: String) {
        val scheduledTask = scheduledTasks[id]
        scheduledTask?.cancel(false)
        scheduledTasks.remove(id)
    }

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler)
    }
}