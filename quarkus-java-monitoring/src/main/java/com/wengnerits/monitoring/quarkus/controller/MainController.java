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

package com.wengnerits.monitoring.quarkus.controller;

import com.wengnerits.monitoring.quarkus.service.HalloService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class MainController {

    private final MeterRegistry registry;
    private final Counter mainCounter;
    private final Counter.Builder nameCounterBuilder;
    private final HalloService halloService;
    private final Map<String, Counter> counters = new HashMap<>();

    MainController(final MeterRegistry registry, final HalloService halloService) {
        this.registry = registry;
        this.mainCounter = Counter.builder("quarkus-main-counter")
                .tag("application", "quarkus")
                .register(registry);
        this.nameCounterBuilder = Counter.builder("quarkus-name-counter")
                .tag("application", "quarkus");
        this.halloService = halloService;
    }

    @GET
    @Produces("text/plain")
    public String hello() {
        mainCounter.increment();
        return halloService.hallo();
    }

    @GET
    @Produces("text/plain")
    @Path("{name}")
    public String halloWithName(@PathParam("name") String name) {
        nameCounterBuilder.tag("name", name).register(registry);
        getNameCounter(name).increment();
        return """
                Hallo name '$name'
                """.replace("$name", name);
    }


    private Counter getNameCounter(String name) {
        if (counters.containsKey(name)) {
            return counters.get(name);
        } else {
            Counter counter = nameCounterBuilder.tag("name", name).register(registry);
            counters.put(name, counter);
            return counter;
        }
    }


}
