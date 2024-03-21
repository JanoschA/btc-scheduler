package com.custom.bitvavo.api

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.json.JSONObject
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.`when`

class BitvavoApiTest {

    @Mock
    private lateinit var bitvavoMock: Bitvavo

    @InjectMocks
    private lateinit var bitvavoApi: BitvavoApi

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        System.setProperty("API_KEY", "ABC")
        System.setProperty("API_SECRET", "ABC")
    }

    @Test
    fun testPlaceOrder() {
        val amount = "0.01"
        val mockResponse = JSONObject(mapOf("result" to "success"))

        // Define the behavior of the mock
        `when`(bitvavoMock.placeOrder(
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.anyString(),
            Mockito.any()
        )).thenReturn(mockResponse)

        // Call the method under test
        bitvavoApi.placeOrder(amount)

        // Verify that the method was called on the mock with the correct parameters
        Mockito.verify(bitvavoMock).placeOrder(
            eq("BTC-EUR"),
            eq("buy"),
            eq("market"),
            any()
        )
    }
}