package io.octatec.horext.api.controller

import io.octatec.horext.api.domain.Subject
import io.octatec.horext.api.dto.Page
import io.octatec.horext.api.service.SubjectService
import io.octatec.horext.api.util.Pagination
import org.ktorm.dsl.map
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestParam

@RestController
@RequestMapping("subjects")
class SubjectController(val subjectService: SubjectService) {

    @GetMapping(params = ["speciality","hourlyLoad"])
    fun getAllBySpeciality(
            @RequestParam(name = "speciality") specialityId:Long,
            @RequestParam(name = "hourlyLoad") hourlyLoadId:Long
    ): ResponseEntity<List<Subject>> {
        return ResponseEntity<List<Subject>>(
                subjectService.getAllBySpecialityId(specialityId,hourlyLoadId),
                HttpStatus.OK)
    }


    @GetMapping(params = ["search","speciality","hourlyLoad"])
    fun getAllBySearch(
            @RequestParam(name = "search", required = true) search:String ,
            @RequestParam(name = "speciality") specialityId:Long,
            @RequestParam(name = "hourlyLoad") hourlyLoadId:Long,
            @RequestParam(name = "offset", defaultValue= "0") offset: Int,
            @RequestParam(name = "limit", defaultValue= "10") limit: Int
    ): ResponseEntity<Page<Subject>> {
        Pagination.validatePageNumberAndSize(offset,limit)
        val page = subjectService.getAllBySearchAndSpecialityIdAndHourlyLoad(
            search,specialityId,hourlyLoadId,offset,limit)
        return ResponseEntity<Page<Subject>>(page , HttpStatus.OK)
    }
}