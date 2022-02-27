/*
 * Copyright (c) 2022, Miroslav Wengner
 *
 * fw-monitoring-examples is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fw-monitoring-examples is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with fw-monitoring-examples. If not, see <http://www.gnu.org/licenses/>.
 */

package com.wengnerits.monitoring.ktor.config

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.Timer
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.core.component.KoinComponent

class MetricsService : KoinComponent {

    private companion object {
        private const val APP_NAME = "ktor-monitoring"
    }

    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
        config().commonTags("application", APP_NAME)
    }

    private val mainCounter = Counter.builder("main")
            .description("main counter")
            .tag("application", APP_NAME)
            .tag("section", "main")
            .register(registry)

    private val timerCounter = Counter.builder("timer")
            .description("timer counter")
            .tag("application", APP_NAME)
            .tag("section", "main")
            .register(registry)

    private val mainTimer: Timer = registry.timer("ktor-monitoring-main-timer")
    private val simpleTimer: Timer = registry.timer("ktor-monitoring-simple-timer")

    fun mainCount() {
        mainCounter.increment()
    }

    fun timerCount() {
        timerCounter.increment()
    }

    fun registryScrape(): String = registry.scrape()

    fun mainTimer() = mainTimer
    fun simpleTimer() = simpleTimer

}
