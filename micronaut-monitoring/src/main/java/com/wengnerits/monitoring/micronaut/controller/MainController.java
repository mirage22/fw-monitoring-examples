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

package com.wengnerits.monitoring.micronaut.controller;

import com.wengnerits.monitoring.micronaut.service.HelloService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.prometheus.client.exporter.common.TextFormat;

import java.util.HashMap;
import java.util.Map;

@Controller("/")
public class MainController {

    private final Counter helloCounter;
    private final Counter.Builder nameCounterBuilder;
    private final HelloService helloService;
    private final PrometheusMeterRegistry registry;

    private final Map<String, Counter> counters = new HashMap<>();

    public MainController(PrometheusMeterRegistry registry, HelloService helloService) {
        this.helloCounter = registry.counter("hello-counter", "application", "micronaut");
        this.nameCounterBuilder = Counter.builder("name-counter").tag("application", "micronaut");
        this.helloService = helloService;
        this.registry = registry;
    }

    @Get(processes = MediaType.TEXT_PLAIN)
    public String hello() {
        helloCounter.increment();
        return helloService.hello();
    }

    @Get(processes = MediaType.TEXT_PLAIN, value = "{name}")
    public String hello(@PathVariable String name) {
        nameCounterBuilder.tag("name", name).register(registry);
        getNameCounter(name).increment();
        return """
                Hello '$name'
                """.replace("$name", name);
    }

    @Get(processes = MediaType.TEXT_PLAIN, value = "/metrics")
    public String metrics() {
        return registry.scrape(TextFormat.CONTENT_TYPE_004);
    }

    private Counter getNameCounter(String name) {
        return counters.computeIfAbsent(name, (k) -> nameCounterBuilder.tag("name", k).register(registry));
    }

}
