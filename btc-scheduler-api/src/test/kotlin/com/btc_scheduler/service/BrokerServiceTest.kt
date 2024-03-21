package com.btc_scheduler.service

import com.btc_scheduler.model.Order
import com.btc_scheduler.repository.OrderRepository
import com.custom.bitvavo.api.BitvavoApi
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class BrokerServiceTest {

    private val bitvavoApiMock = Mockito.mock(BitvavoApi::class.java)
    private val orderRepository = Mockito.mock(OrderRepository::class.java)
    private val brokerService = BrokerService(bitvavoApiMock, orderRepository)

    @Test
    fun `test buy`() {
        val amount = "0.01"
        val expectedOrderId = "123456789"

        `when`(bitvavoApiMock.placeOrder(amount)).thenReturn(JSONObject(mapOf("orderId" to expectedOrderId)))

        brokerService.buy(amount)

        Mockito.verify(bitvavoApiMock).placeOrder(amount)

        val orderCaptor = ArgumentCaptor.forClass(Order::class.java)
        Mockito.verify(orderRepository).save(orderCaptor.capture())

        Assertions.assertEquals(expectedOrderId, orderCaptor.value.orderId)
    }
}