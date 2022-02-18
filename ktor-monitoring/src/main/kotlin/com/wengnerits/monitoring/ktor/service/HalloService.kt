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