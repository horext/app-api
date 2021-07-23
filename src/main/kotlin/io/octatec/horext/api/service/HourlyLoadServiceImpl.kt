package io.octatec.horext.api.service

import io.octatec.horext.api.model.HourlyLoad
import io.octatec.horext.api.model.OrganizationUnit
import io.octatec.horext.api.repository.HourlyLoadRepository
import org.springframework.stereotype.Service

@Service
class HourlyLoadServiceImpl(val hourlyLoadRepository: HourlyLoadRepository) : HourlyLoadService {
    override fun getLatestByFaculty(facultyId: Long): HourlyLoad {
        val hourlyLoad = hourlyLoadRepository.getLatestByOrganizationUnitId(facultyId)
        return HourlyLoad(hourlyLoad.id,hourlyLoad.publishedAt)
    }
}