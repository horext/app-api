package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.service.HourlyLoadService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("hourlyLoads")
class HourlyLoadController(val hourlyLoadService: HourlyLoadService) {

    @GetMapping("/latest")
    fun getLatestBySpeciality(
            @RequestParam(name = "faculty") facultyId:Long
    ): ResponseEntity<HourlyLoad> {
        return try {
            ResponseEntity<HourlyLoad>(
                hourlyLoadService.getLatestByFaculty(facultyId),
                HttpStatus.OK)
        }catch (e: Exception){

            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}