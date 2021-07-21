package io.octatec.horext.api.service

import io.octatec.horext.api.model.Subject
import org.springframework.util.MultiValueMap

interface SubjectService {
    fun getAllBySpecialityId(specialityId: Long, hourlyLoadId: Long): List<Subject>
}