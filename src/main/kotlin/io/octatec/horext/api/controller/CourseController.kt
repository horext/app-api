package io.octatec.horext.api.controller

import io.octatec.horext.api.config.AppConstants
import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Course
import io.octatec.horext.api.service.CourseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("courses")
class CourseController(val service: CourseService) {


    @GetMapping(params = ["search"])
    fun getPolls(
        @RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) page: Int,
        @RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) size: Int,
        @RequestParam(value = "search", required = true) search: String
    ): PageDTO<Course> {
        return if(search.isNotEmpty())
            service.getAllBySearch(page, size, search)
        else
            service.getAll(page, size)
    }

}