package io.octatec.horext.api.controller

import io.octatec.horext.api.config.AppConstants
import io.octatec.horext.api.dto.PageDTO
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

    @GetMapping(params = ["search"])
    fun getPolls(
        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) page: Int,
        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) size: Int,
        @RequestParam(value = "search", required = true) search: String
    ): PageDTO<Subject> {
        return if(search.isNotEmpty())
            subjectService.getAllBySearch(page, size, search)
        else
            subjectService.getAll(page, size)
    }
}