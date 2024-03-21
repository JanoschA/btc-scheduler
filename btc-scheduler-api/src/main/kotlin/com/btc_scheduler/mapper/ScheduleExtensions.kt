package com.btc_scheduler.mapper

import com.btc_scheduler.model.ResponseSchedule
import com.btc_scheduler.model.Schedule

object ScheduleExtensions {

    fun Schedule.toResponseSchedule(): ResponseSchedule {
        return ResponseSchedule(
            id = this.id!!,
            cron = this.cron,
            amount = this.amount,
            active = this.active
        )
    }

    fun List<Schedule>.toResponseSchedule(): List<ResponseSchedule> {
        return this.map { schedule -> schedule.toResponseSchedule() }
    }

}