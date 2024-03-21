package com.btc_scheduler.controller

import com.btc_scheduler.mapper.ScheduleExtensions.toResponseSchedule
import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.ResponseSchedule
import com.btc_scheduler.model.Schedule
import com.btc_scheduler.service.SchedulerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(SchedulerController::class)
class SchedulerControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockBean
    lateinit var schedulerService: SchedulerService

    @Test
    @Throws(Exception::class)
    fun testCreateSchedule() {
        val requestSchedule = RequestSchedule("0 0 8 * * *", "5", true)
        val responseSchedule = ResponseSchedule("1", "0 0 8 * * *", "5", true)
        `when`(schedulerService.createSchedule(requestSchedule)).thenReturn(responseSchedule)

        mockMvc.perform(
            post("/schedules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestSchedule))
        )
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(responseSchedule)))
    }

    @Test
    fun testDeleteSchedule() {
        mockMvc.perform(delete("/schedules/{id}", "1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Schedule deleted successfully"))
    }

    @Test
    fun testListAllSchedules() {
        val responseSchedule1 = Schedule("0 0 8 * * *", "5", true)
        val responseSchedule2 = Schedule("0 0 8 * * *", "5", true)
        val schedules = listOf(responseSchedule1, responseSchedule2)
        `when`(schedulerService.listAllSchedules()).thenReturn(schedules)

        mockMvc.perform(get("/schedules"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(schedules.toResponseSchedule())))
    }
}