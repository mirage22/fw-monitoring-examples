package xyz.chrisime.monitoring.spring.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApplicationMain

fun main(args: Array<String>) {
    runApplication<ApplicationMain>(*args)
}
