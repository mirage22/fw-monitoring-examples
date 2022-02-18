package com.wengnerits.monitoring.ktor.service

import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit.SECONDS

interface HalloService : KoinComponent {
    fun hallo(): String
    fun init(): Boolean
    fun action(): Unit
}

class HalloServiceImpl() : HalloService {

    private fun randomInt(): Long {
        return ThreadLocalRandom.current().nextInt(0, 10).toLong()
    }

    override fun hallo() = "Welcome KTor monitoring example"
    override fun init(): Boolean {
        runBlocking {
            SECONDS.sleep(randomInt())
        }
        return true
    }

    override fun action() {
        runBlocking {
            SECONDS.sleep(randomInt())
        }
    }
}