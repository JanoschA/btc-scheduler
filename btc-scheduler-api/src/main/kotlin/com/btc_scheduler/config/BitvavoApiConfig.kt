package com.btc_scheduler.config

import com.custom.bitvavo.api.BitvavoApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BitvavoApiConfig {

    @Bean
    open fun bitvavoApi(): BitvavoApi {
        return BitvavoApi()
    }
}