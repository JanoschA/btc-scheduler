package com.btc_scheduler.model

data class ResponseSchedule(
    val id: String,
    val cron: String,
    val amount: String,
    val active: Boolean
)
