package com.btc_scheduler.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Schedule() {
    @Id var id: String? = null

    /**
     * Spring Cron Format:
     * 1. Seconds (0-59)
     * 2. Minutes (0-59)
     * 3. Hours (0-23)
     * 4. Day of month (1-31)
     * 5. Month (1-12)
     * 6. Day of week (0-7 where both 0 and 7 are Sunday)
     * */
    var cron: String = "" // e.g. "0 0 8 * * *"
    var amount: String = ""
    var active: Boolean = false

    constructor(cron: String, amount: String, active: Boolean) : this() {
        this.id = UUID.randomUUID().toString()
        this.cron = cron
        this.amount = amount
        this.active = active
    }
}