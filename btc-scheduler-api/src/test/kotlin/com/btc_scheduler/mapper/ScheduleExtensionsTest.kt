package com.btc_scheduler.mapper

import com.btc_scheduler.mapper.ScheduleExtensions.toResponseSchedule
import com.btc_scheduler.model.ResponseSchedule
import com.btc_scheduler.model.Schedule
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ScheduleExtensionsTest {

    @Test
    fun `test toResponseSchedule`() {
        val schedule = Schedule("0 0 8 * * *", "5", true)
        val expectedResponseSchedule = ResponseSchedule(schedule.id!!, "0 0 8 * * *", "5", true)

        val actualResponseSchedule = schedule.toResponseSchedule()

        assertTrue(responseSchedulesAreEqual(expectedResponseSchedule, actualResponseSchedule))
    }

    @Test
    fun `test toResponseSchedule with list`() {
        val schedule1 = Schedule("0 0 8 * * *", "5", true)
        val schedule2 = Schedule("0 0 8 * * *", "15", true)
        val schedules = listOf(schedule1, schedule2)
        val expectedResponseSchedules = listOf(
            ResponseSchedule(schedule1.id!!, "0 0 8 * * *", "5", true),
            ResponseSchedule(schedule2.id!!, "0 0 8 * * *", "15", true)
        )

        val actualResponseSchedules = schedules.toResponseSchedule()

        assertTrue(actualResponseSchedules.size == expectedResponseSchedules.size &&
                actualResponseSchedules.zip(expectedResponseSchedules).all { (a, e) -> responseSchedulesAreEqual(a, e) })
    }

    private fun responseSchedulesAreEqual(rs1: ResponseSchedule, rs2: ResponseSchedule): Boolean {
        return rs1.id == rs2.id && rs1.cron == rs2.cron && rs1.amount == rs2.amount && rs1.active == rs2.active
    }
}