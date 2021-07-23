package io.octatec.horext.api.service

import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Subject

interface SubjectService {
    fun getAllBySpecialityId(specialityId: Long, hourlyLoadId: Long): List<Subject>

    fun getAllBySearch(page: Int, size: Int, search: String): PageDTO<Subject>

    fun getAll(page: Int, size: Int): PageDTO<Subject>
}