package io.octatec.horext.api.service

import io.octatec.horext.api.model.HourlyLoad
import org.springframework.util.MultiValueMap

interface HourlyLoadService {
    fun getLatestByFaculty(facultyId: Long): HourlyLoad
}