/*
 * Copyright (c) 2022, Miroslav Wengner(mirage22), Christian Meyer (chrisme)
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

package xyz.chrisime.monitoring.micronaut.controller

import io.micrometer.core.instrument.Counter
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.prometheus.client.exporter.common.TextFormat
import xyz.chrisime.monitoring.micronaut.service.HelloService

@Controller
class MainController(private val registry: PrometheusMeterRegistry, private val helloService: HelloService) {

    private val helloCounter = registry.counter("hello-counter", "application", "micronaut-kt")
    private val nameCounterBuilder = Counter.builder("name-counter").tag("application", "micronaut-kt")
    private val counters = mutableMapOf<String, Counter>()

    @Get(processes = [MediaType.TEXT_PLAIN])
    fun hello(): String {
        helloCounter.increment()
        return helloService.hello()
    }

    @Get(processes = [MediaType.TEXT_PLAIN], value = "{name}")
    fun hello(@PathVariable name: String): String {
        counters.getOrPut(name) {
            nameCounterBuilder.tag("name", name).register(registry)
        }.increment()

        return "Hello '$name'"
    }

    @Get(processes = [MediaType.TEXT_PLAIN], value = "/metrics")
    fun metrics(): String = registry.scrape(TextFormat.CONTENT_TYPE_004)

}
