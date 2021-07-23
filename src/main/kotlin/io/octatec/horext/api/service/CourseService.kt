package io.octatec.horext.api.service

import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Course

interface CourseService {
    fun getAllBySearch(page: Int, size: Int, search: String): PageDTO<Course>
    fun getAll(page: Int, size: Int): PageDTO<Course>
}