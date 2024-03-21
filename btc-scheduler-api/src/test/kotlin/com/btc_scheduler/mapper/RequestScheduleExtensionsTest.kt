package com.btc_scheduler.mapper

import com.btc_scheduler.mapper.RequestScheduleExtensions.toSchedule
import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.Schedule
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RequestScheduleExtensionsTest {

    @Test
    fun `test toSchedule`() {
        val requestSchedule = RequestSchedule("0 0 8 * * *", "5", true)
        val expectedSchedule = Schedule("0 0 8 * * *", "5", true)

        val actualSchedule = requestSchedule.toSchedule()

        assertTrue(schedulesAreEqual(expectedSchedule, actualSchedule))
    }

    @Test
    fun `test toSchedule with list`() {
        val requestSchedule1 = RequestSchedule("0 0 8 * * *", "5", true)
        val requestSchedule2 = RequestSchedule("0 0 8 * * *", "5", true)
        val requestSchedules = listOf(requestSchedule1, requestSchedule2)
        val expectedSchedules = listOf(
            Schedule("0 0 8 * * *", "5", true),
            Schedule("0 0 8 * * *", "5", true)
        )

        val actualSchedules = requestSchedules.toSchedule()

        assertTrue(actualSchedules.size == expectedSchedules.size &&
                actualSchedules.zip(expectedSchedules).all { (a, e) -> schedulesAreEqual(a, e) })
    }

    private fun schedulesAreEqual(s1: Schedule, s2: Schedule): Boolean {
        return s1.cron == s2.cron && s1.amount == s2.amount && s1.active == s2.active
    }
}