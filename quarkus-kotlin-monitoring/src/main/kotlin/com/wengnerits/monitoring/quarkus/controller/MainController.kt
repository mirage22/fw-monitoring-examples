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
package com.wengnerits.monitoring.quarkus.controller

import com.wengnerits.monitoring.quarkus.service.HalloService
import io.micrometer.core.instrument.Counter
import io.micrometer.prometheus.PrometheusMeterRegistry
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces

@Path("/")
class MainController @Inject constructor(
        private val registry: PrometheusMeterRegistry,
        private val halloService: HalloService
) {

    private val mainCounter = registry.counter("quarkus-main-counter", "application", "quarkus")
    private val nameCounterBuilder = Counter.builder("quarkus-name-counter").tag("application", "quarkus")
    private val counters = mutableMapOf<String, Counter>()

    @GET
    @Produces("text/plain")
    fun hello(): String {
        mainCounter.increment()
        return halloService.hallo()
    }

    @GET
    @Produces("text/plain")
    @Path("hallo/{name}")
    fun halloWithName(@PathParam("name") name: String): String {
        counters.getOrPut(name) {
            nameCounterBuilder.tag("name", name).register(registry)
        }.increment()
        return "Hallo '$name'"
    }

}
