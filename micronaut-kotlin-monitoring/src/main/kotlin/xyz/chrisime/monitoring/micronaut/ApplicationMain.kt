package xyz.chrisime.monitoring.micronaut

import io.micronaut.runtime.Micronaut

object ApplicationMain {
    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.run(*args)
    }
}
