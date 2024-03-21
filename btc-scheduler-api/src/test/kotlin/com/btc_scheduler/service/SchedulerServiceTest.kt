package com.btc_scheduler.service

import com.btc_scheduler.mapper.RequestScheduleExtensions.toSchedule
import com.btc_scheduler.mapper.ScheduleExtensions.toResponseSchedule
import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.Schedule
import com.btc_scheduler.repository.SchedulerRepository
import com.btc_scheduler.scheduler.DynamicTaskScheduler
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.*

class SchedulerServiceTest {

    private val schedulerRepository = mock(SchedulerRepository::class.java)
    private val dynamicTaskScheduler = mock(DynamicTaskScheduler::class.java)
    private val schedulerService = SchedulerService(schedulerRepository, dynamicTaskScheduler)

    @Test
    fun `test createSchedule`() {
        val requestSchedule = RequestSchedule("0 0 8 * * *", "5", true)
        val expectedSchedule = requestSchedule.toSchedule()
        val expectedResponse = expectedSchedule.toResponseSchedule()

        `when`(schedulerRepository.save(any(Schedule::class.java))).thenReturn(expectedSchedule)

        val actualResponse = schedulerService.createSchedule(requestSchedule)

        assertEquals(expectedResponse, actualResponse)
        verify(schedulerRepository).save(any(Schedule::class.java))
        verify(dynamicTaskScheduler).scheduleTask(expectedSchedule)
    }

    @Test
    fun `test createSchedule but inactive`() {
        val requestSchedule = RequestSchedule("0 0 8 * * *", "5", false)
        val expectedSchedule = requestSchedule.toSchedule()
        val expectedResponse = expectedSchedule.toResponseSchedule()

        `when`(schedulerRepository.save(any(Schedule::class.java))).thenReturn(expectedSchedule)

        val actualResponse = schedulerService.createSchedule(requestSchedule)

        assertEquals(expectedResponse, actualResponse)
        verify(schedulerRepository).save(any(Schedule::class.java))
        verify(dynamicTaskScheduler, times(0)).scheduleTask(expectedSchedule)
    }

    @Test
    fun `test deleteSchedule`() {
        val id = "testId"

        schedulerService.deleteSchedule(id)

        verify(schedulerRepository).deleteById(id)
        verify(dynamicTaskScheduler).cancelTask(id)
    }

    @Test
    fun `test listAllSchedules`() {
        val requestSchedule1 = RequestSchedule("0 0 8 * * *", "5", true)
        val requestSchedule2 = RequestSchedule("0 0 8 * * *", "5", true)
        val expectedSchedules = listOf(requestSchedule1, requestSchedule2).toSchedule()

        `when`(schedulerRepository.findAll()).thenReturn(expectedSchedules)

        val actualResponses = schedulerService.listAllSchedules()

        assertEquals(expectedSchedules, actualResponses)
        verify(schedulerRepository).findAll()
    }
}