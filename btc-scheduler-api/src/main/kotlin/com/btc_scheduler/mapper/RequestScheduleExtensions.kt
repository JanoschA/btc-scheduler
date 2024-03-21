package com.btc_scheduler.mapper

import com.btc_scheduler.model.RequestSchedule
import com.btc_scheduler.model.Schedule

object RequestScheduleExtensions {

    fun RequestSchedule.toSchedule(): Schedule {
        return Schedule(
            cron = this.cron,
            amount = this.amount,
            active = this.active
        )
    }

    fun List<RequestSchedule>.toSchedule(): List<Schedule> {
        return this.map { requestSchedule -> requestSchedule.toSchedule() }
    }

}