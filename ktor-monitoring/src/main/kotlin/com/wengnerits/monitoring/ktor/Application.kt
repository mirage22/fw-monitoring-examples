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

package com.wengnerits.monitoring.ktor

import com.wengnerits.monitoring.ktor.config.appModule
import com.wengnerits.monitoring.ktor.route.registerMainRoutes
import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.routing.Routing
import io.ktor.serialization.json
import io.ktor.server.netty.EngineMain
import org.koin.ktor.ext.Koin

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(testing: Boolean = false) {

    install(Koin) {
        modules(
            listOf(
                appModule
            )
        )
    }

    install(ContentNegotiation) {
        json()
    }

    install(Routing) {
        intercept(ApplicationCallPipeline.Call) {
            println("application-monitoring called")
        }
    }

    registerMainRoutes()
}
