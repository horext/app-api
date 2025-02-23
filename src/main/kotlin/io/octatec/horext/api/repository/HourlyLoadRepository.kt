package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.HourlyLoad

interface HourlyLoadRepository {
    fun getLatestByFaculty(facultyId: Long): HourlyLoad
}
