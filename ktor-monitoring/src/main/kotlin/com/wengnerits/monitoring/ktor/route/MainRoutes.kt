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

package com.wengnerits.monitoring.ktor.route

import com.wengnerits.monitoring.ktor.config.MetricsService
import com.wengnerits.monitoring.ktor.service.HelloServiceImpl
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import io.ktor.metrics.micrometer.MicrometerMetrics
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmHeapPressureMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.prometheus.client.exporter.common.TextFormat
import kotlinx.coroutines.runBlocking
import org.koin.ktor.ext.inject
import java.util.concurrent.TimeUnit

fun Route.getMainRoutes() {

    val helloService: HelloServiceImpl by inject()
    val metricsService: MetricsService by inject()

    get("/") {
        metricsService.mainTimer().record {
            metricsService.helloCount()
            runBlocking {
                call.respondText(helloService.hello(), status = HttpStatusCode.OK)
            }
        }
    }

    get("/{name}") {
        metricsService.nameCount()
        call.respondText(helloService.name(call.parameters["name"]), status = HttpStatusCode.OK)
    }

    get("/timer") {
        metricsService.simpleTimer().record() {
            runBlocking {
                if (helloService.init()) {
                    helloService.action()
                }
                call.respondText("This is simple timer", status = HttpStatusCode.OK)
            }
        }
    }

    get("/timer_long") {
        metricsService.simpleTimer().record() {
            runBlocking {
                TimeUnit.SECONDS.sleep(1)
                call.respondText("This is simple timer", status = HttpStatusCode.OK)
            }
        }
    }
}

fun Application.registerMainRoutes() {

    val metricsService: MetricsService by inject()

    install(MicrometerMetrics) {
        registry = metricsService.registry
        meterBinders = listOf(
            ClassLoaderMetrics(),
            JvmMemoryMetrics(),
            JvmHeapPressureMetrics(),
            JvmThreadMetrics(),
            JvmGcMetrics(),
            FileDescriptorMetrics(),
            ProcessorMetrics(),
            UptimeMetrics()
        )
    }

    routing {
        getMainRoutes()

        get("/metrics") {
            call.respond(
                TextContent(
                    metricsService.registryScrape(),
                    ContentType.parse(TextFormat.CONTENT_TYPE_004)
                )
            )
        }
    }
}