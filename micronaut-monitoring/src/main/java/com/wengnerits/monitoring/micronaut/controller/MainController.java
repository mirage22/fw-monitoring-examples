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


import com.wengnerits.monitoring.micronaut.service.HalloService;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import jakarta.inject.Inject;

@Controller("/")
public class MainController {

    private final PrometheusMeterRegistry registry;
    private final HalloService halloService;

    @Inject
    public MainController(PrometheusMeterRegistry registry, HalloService halloService) {
        this.registry = registry;
        this.halloService = halloService;
    }

    @Get(processes = MediaType.TEXT_PLAIN)
    public String hello() {
        return halloService.hallo();
    }

    @Get(uri = "/prometheus", processes = MediaType.TEXT_PLAIN)
    public String prometheus(){
        return registry.scrape();
    }
}
