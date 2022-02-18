package com.wengnerits.monitoring.ktor.config

import com.wengnerits.monitoring.ktor.service.HalloServiceImpl
import io.ktor.util.KtorExperimentalAPI
import org.koin.dsl.module

@KtorExperimentalAPI
val appModule = module {
    single { HalloServiceImpl() }
    single { MetricsService() }
}