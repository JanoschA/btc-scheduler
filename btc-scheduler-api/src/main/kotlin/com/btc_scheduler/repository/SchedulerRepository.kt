package com.btc_scheduler.repository

import com.btc_scheduler.model.Schedule
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface SchedulerRepository : MongoRepository<Schedule, String> {
}