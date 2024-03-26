package io.octatec.horext.api.service

import io.octatec.horext.api.domain.HourlyLoad

interface HourlyLoadService {
    fun getLatestByFaculty(facultyId: Long): HourlyLoad
}