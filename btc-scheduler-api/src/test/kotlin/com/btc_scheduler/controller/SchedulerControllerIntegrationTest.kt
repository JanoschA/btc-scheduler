package com.btc_scheduler.controller

import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.Schedule
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class SchedulerControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    companion object {
        @Container
        val mongoDBContainer: MongoDBContainer = MongoDBContainer("mongo:4.4.6")

        @JvmStatic
        @DynamicPropertySource
        fun setProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl)
        }
    }
    @BeforeEach
    fun setUp() {
        mongoDBContainer.start()
    }

    @AfterEach
    fun tearDown() {
        mongoDBContainer.stop()
    }

    @Test
    fun `test createSchedule`() {
        val requestSchedule = RequestSchedule("0 0 8 * * *", "5", true)
        val requestScheduleJson = objectMapper.writeValueAsString(requestSchedule)

        mockMvc.perform(post("/schedules")
            .contentType("application/json")
            .content(requestScheduleJson))
            .andExpect(status().isOk)

        val savedSchedule = mongoTemplate.findOne(Query(), Schedule::class.java)
        Assertions.assertNotNull(savedSchedule)
        Assertions.assertEquals(requestSchedule.cron, savedSchedule?.cron)
        Assertions.assertEquals(requestSchedule.amount, savedSchedule?.amount)
        Assertions.assertEquals(requestSchedule.active, savedSchedule?.active)
    }
}