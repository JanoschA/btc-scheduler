package com.btc_scheduler.model

data class RequestSchedule(
    val cron: String,
    val amount: String,
    val active: Boolean
)