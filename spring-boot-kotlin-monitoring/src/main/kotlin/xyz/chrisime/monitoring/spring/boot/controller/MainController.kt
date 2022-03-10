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

package xyz.chrisime.monitoring.spring.boot.controller

import io.micrometer.core.instrument.Counter
import io.micrometer.prometheus.PrometheusMeterRegistry
import io.prometheus.client.exporter.common.TextFormat.CONTENT_TYPE_004
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import xyz.chrisime.monitoring.spring.boot.service.HelloService

@RestController("/")
class MainController(private val helloService: HelloService, private val registry: PrometheusMeterRegistry) {

    private val helloCounter = registry.counter("hello-counter", "application", "spring-boot-kt")
    private var nameCounterBuilder = Counter.builder("name-counter").tag("application", "spring-boot-kt")

    private val counters = mutableMapOf<String, Counter>()

    @GetMapping(produces = [MediaType.TEXT_PLAIN_VALUE])
    fun hello(): String {
        helloCounter.increment()
        return helloService.hello()
    }

    @GetMapping(value = ["{name}"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun hello(@PathVariable name: String): String {
        counters.getOrPut(name) {
            nameCounterBuilder.tag("name", name).register(registry)
        }.increment()

        return "Hello '$name'"
    }

    @GetMapping(value = ["metrics"], produces = [MediaType.TEXT_PLAIN_VALUE])
    fun metrics(): String? {
        return registry.scrape(CONTENT_TYPE_004)
    }

}
