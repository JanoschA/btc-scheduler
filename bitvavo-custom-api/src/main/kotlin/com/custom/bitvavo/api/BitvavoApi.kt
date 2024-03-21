package com.custom.bitvavo.api

import org.json.JSONObject

class BitvavoApi {

    private var bitvavo: Bitvavo
    private val apiKey: String = System.getenv("API_KEY") ?: System.getProperty("API_KEY") ?: ""
    private val apiSecret: String = System.getenv("API_SECRET") ?: System.getProperty("API_SECRET") ?: ""
    private val baseUrl: String = "https://api.bitvavo.com/v2"

    init {
        this.bitvavo = Bitvavo(apiKey, apiSecret, baseUrl)
    }

    fun placeOrder(amount: String): JSONObject {
        return bitvavo.placeOrder("BTC-EUR", "buy", "market", JSONObject(mapOf("amount" to amount)))
    }
}