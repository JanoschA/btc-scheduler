package com.btc_scheduler.service

import com.btc_scheduler.model.Order
import com.btc_scheduler.repository.OrderRepository
import com.custom.bitvavo.api.BitvavoApi
import org.springframework.stereotype.Service

@Service
class BrokerService(
    private val bitvavoApi: BitvavoApi,
    private val orderRepository: OrderRepository
) {

    fun buy(amount: String) {
        val response = bitvavoApi.placeOrder(amount)

        orderRepository.save(Order().apply {
            orderId = response.getString("orderId")
            // TODO: save more fields
        })
    }
}