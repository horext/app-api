package io.octatec.horext.api.controller

import io.octatec.horext.api.model.Subject
import io.octatec.horext.api.service.SubjectService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("subjects")
class SubjectController(val subjectService: SubjectService) {

    @GetMapping
    fun getAllBySpeciality(
            @RequestParam(name = "speciality") specialityId:Long,
            @RequestParam(name = "hourlyLoad") hourlyLoadId:Long
    ): ResponseEntity<List<Subject>> {
        return ResponseEntity<List<Subject>>(
                subjectService.getAllBySpecialityId(specialityId,hourlyLoadId),
                HttpStatus.OK)
    }
}