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
            println("application-montioring called")
        }
    }

    registerMainRoutes()
}