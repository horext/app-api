package io.octatec.horext.api.service

import io.octatec.horext.api.dto.PageDTO
import io.octatec.horext.api.model.Subject
import org.springframework.data.domain.Pageable

interface SubjectService {
    fun getAllBySearch(pageable: Pageable, search: String, specialityId: Long, hourlyLoadId: Long): PageDTO<Subject>

    fun getAll(pageable: Pageable, specialityId: Long, hourlyLoadId: Long): PageDTO<Subject>
}