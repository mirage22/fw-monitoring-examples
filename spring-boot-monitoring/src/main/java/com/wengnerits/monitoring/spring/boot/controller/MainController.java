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

package com.wengnerits.monitoring.spring.boot.controller;

import com.wengnerits.monitoring.spring.boot.service.HelloService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.exporter.common.TextFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("/")
public class MainController {

    private final Counter helloCounter;
    private final Counter.Builder nameCounterBuilder;
    private final HelloService helloService;
    private final PrometheusMeterRegistry registry;

    private final Map<String, Counter> counters = new HashMap<>();

    @Autowired
    public MainController(PrometheusMeterRegistry registry, HelloService helloService) {
        this.helloCounter = registry.counter("index-counter", "application", "spring-boot");
        this.nameCounterBuilder = Counter.builder("name-counter").tag("application", "spring-boot");
        this.helloService = helloService;
        this.registry = registry;
    }

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello() {
        helloCounter.increment();
        return helloService.hello();
    }

    @GetMapping(value = "{name}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String hello(@PathVariable String name) {
        nameCounterBuilder.tag("name", name).register(registry);
        getNameCounter(name).increment();
        return """
            Hello '$name'
            """.replace("$name", name);
    }

    @GetMapping(value = "metrics", produces = MediaType.TEXT_PLAIN_VALUE)
    public String metrics() {
        return registry.scrape(TextFormat.CONTENT_TYPE_004);
    }

    private Counter getNameCounter(String name) {
        return counters.computeIfAbsent(name, (k) -> nameCounterBuilder.tag("name", k).register(registry));
    }
}
