package io.octatec.horext.api.service

import io.octatec.horext.api.domain.Schedule
import io.octatec.horext.api.repository.ScheduleRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScheduleServiceImpl(private val scheduleRepository: ScheduleRepository) : ScheduleService {

    override fun findBySubjectIdAndHourlyLoadId(subjectId: Long, hourlyLoadId: Long): List<Schedule> {
        return scheduleRepository.findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)
    }
}
