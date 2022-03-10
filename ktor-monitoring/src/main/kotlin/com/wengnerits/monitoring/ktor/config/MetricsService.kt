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
import io.micrometer.core.instrument.LongTaskTimer
import io.micrometer.core.instrument.Timer
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry
import org.koin.core.component.KoinComponent

class MetricsService : KoinComponent {

    companion object {
        const val APP_NAME = "ktor-monitoring"
    }

    val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT).apply {
        config().commonTags("application", APP_NAME)
    }

    private val helloCounter = Counter
        .builder("hello-counter")
        .description("hello counter")
        .tag("application", APP_NAME)
        .tag("section", "hello")
        .register(registry)

    private val nameCounter = Counter
        .builder("name-counter")
        .description("name counter")
        .tag("application", APP_NAME)
        .tag("section", "name")
        .register(registry)

    private val mainTimer: Timer = registry.timer("monitoring-main-timer")
    private val simpleTimer: Timer = registry.timer("monitoring-simple-timer")
    private val longTaskTimer: LongTaskTimer = LongTaskTimer.builder("monitoring-long-task-timer").register(registry)

    fun helloCount() {
        helloCounter.increment()
    }

    fun nameCount(){
        nameCounter.increment()
    }

    fun registryScrape(): String = registry.scrape()

    fun mainTimer() = mainTimer
    fun simpleTimer() = simpleTimer
    fun taskTimer() = longTaskTimer
}