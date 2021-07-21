package io.octatec.horext.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiForHorextApplication

fun main(args: Array<String>) {
	runApplication<ApiForHorextApplication>(*args)
}
