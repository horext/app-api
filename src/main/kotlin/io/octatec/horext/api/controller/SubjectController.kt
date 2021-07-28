package io.octatec.horext.api.controller

import io.octatec.horext.api.config.AppConstants
import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Subject
import io.octatec.horext.api.service.SubjectService
import io.octatec.horext.api.util.Pagination
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    fun getPolls(
        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) page: Int,
        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) size: Int,
        @RequestParam(value = "search", defaultValue = "") search: String,
        @RequestParam(name = "speciality") specialityId:Long,
        @RequestParam(name = "hourlyLoad") hourlyLoadId:Long
    ): PageDTO<Subject> {
        Pagination.validatePageNumberAndSize(page, size)
        val pageable: Pageable = PageRequest.of(page, size)
        println(search)

        return if(search.isNotEmpty())
            subjectService.getAllBySearch(pageable, search,specialityId,hourlyLoadId)
        else
            subjectService.getAll(pageable,specialityId,hourlyLoadId)
    }
}