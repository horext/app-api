package io.octatec.horext.api

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [ExposedAutoConfiguration::class])
class HorextApiApplication

fun main(args: Array<String>) {
    runApplication<HorextApiApplication>(*args)
}
