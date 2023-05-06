package io.octatec.horext.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HorextApiApplication

fun main(args: Array<String>) {
	runApplication<HorextApiApplication>(*args)
}
