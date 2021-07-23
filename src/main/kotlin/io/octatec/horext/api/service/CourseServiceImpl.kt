package io.octatec.horext.api.service

import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Course
import io.octatec.horext.api.repository.CourseRepository
import io.octatec.horext.api.util.Pagination
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

@Service
class CourseServiceImpl(
    val repository: CourseRepository
) : CourseService {

    override
    fun getAllBySearch(page: Int, size: Int, search: String): PageDTO<Course> {
        Pagination.validatePageNumberAndSize(page, size)
        val pageable: Pageable = PageRequest.of(page, size)
        val courses: Page<Course> = repository.getAllBySearch(search, pageable)
        val body: MutableList<Course> = courses.stream().map { c -> Course(c.id, c.name) }.collect(Collectors.toList())
        return PageDTO(
            body,
            courses.number, courses.size,
            courses.totalElements,
            courses.totalPages, courses.isLast
        )
    }

    override
    fun getAll(page: Int, size: Int): PageDTO<Course> {
        Pagination.validatePageNumberAndSize(page, size)
        val pageable: Pageable = PageRequest.of(page, size)
        val courses: Page<Course> = repository.findAll(pageable)
        val body: MutableList<Course> = courses.stream().map { c -> Course(c.id, c.name) }.collect(Collectors.toList())
        return PageDTO(
            body,
            courses.number, courses.size,
            courses.totalElements,
            courses.totalPages, courses.isLast
        )
    }

}