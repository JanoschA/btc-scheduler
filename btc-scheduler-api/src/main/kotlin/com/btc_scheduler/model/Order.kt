package com.btc_scheduler.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID

@Entity
class Order() {
    @Id var id: String = UUID.randomUUID().toString()

    var orderId: String = ""
}