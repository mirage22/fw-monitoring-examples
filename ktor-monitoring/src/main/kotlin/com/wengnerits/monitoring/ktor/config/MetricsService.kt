package com.wengnerits.monitoring.ktor.config

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.LongTaskTimer
import io.micrometer.core.instrument.Timer
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.core.component.KoinComponent

class MetricsService : KoinComponent {

    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
        config().commonTags("application", "ktor-monitoring")
    }

    private val mainCounter = Counter
        .builder("main")
        .description("simple counter")
        .tag("application", "counting")
        .tag("section", "main")
        .register(registry)

    private val mainTimer: Timer = registry.timer("ktor-monitoring-main-timer")
    private val simpleTimer: Timer = registry.timer("ktor-monitoring-simple-timer")
    private val longTaskTimer: LongTaskTimer = LongTaskTimer.builder("ktor-monitoring-long-task-timer").register(registry)

    fun mainCount() {
        mainCounter.increment()
    }

    fun registryScrape(): String = registry.scrape()

    fun mainTimer() = mainTimer
    fun simpleTimer() = simpleTimer
    fun taskTimer() = longTaskTimer
}