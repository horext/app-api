package io.octatec.horext.api.service

import io.octatec.horext.api.domain.HourlyLoad
import io.octatec.horext.api.repository.HourlyLoadRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class HourlyLoadServiceImpl(private val hourlyLoadRepository: HourlyLoadRepository) : HourlyLoadService {

    override fun getLatestByFaculty(facultyId: Long): HourlyLoad {
        return hourlyLoadRepository.getLatestByFaculty(facultyId)
    }
}
