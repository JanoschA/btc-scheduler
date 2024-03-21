package com.btc_scheduler.scheduler

import com.btc_scheduler.model.Schedule
import com.btc_scheduler.repository.SchedulerRepository
import com.btc_scheduler.service.BrokerService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.Trigger
import java.util.concurrent.ScheduledFuture

class DynamicTaskSchedulerTest {

    private val schedulerRepository: SchedulerRepository = mock()
    private val taskScheduler: TaskScheduler = mock()
    private val brokerService: BrokerService = mock()

    private val dynamicTaskScheduler = DynamicTaskScheduler(schedulerRepository, taskScheduler, brokerService)

    @Test
    fun `should schedule task when scheduleTask is called`() {
        // Arrange
        val schedule = Schedule("0 0 * * * ?", "5", true)
        val scheduledFuture: ScheduledFuture<*> = mock()

        // This tells Mockito to return the mock ScheduledFuture when the schedule method is called
        Mockito.`when`(taskScheduler.schedule(any<Runnable>(), any<Trigger>())).thenReturn(scheduledFuture)

        // Act
        dynamicTaskScheduler.scheduleTask(schedule)

        // Assert
        verify(taskScheduler).schedule(any<Runnable>(), any<Trigger>())
    }
}