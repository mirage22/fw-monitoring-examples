package xyz.chrisime.monitoring.micronaut

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build().args(*args).packages("xyz.chrisime").start()
}
