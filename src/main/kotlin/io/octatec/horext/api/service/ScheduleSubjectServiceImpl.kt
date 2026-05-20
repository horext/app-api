package io.octatec.horext.api.service

import io.octatec.horext.api.domain.ScheduleSubject
import io.octatec.horext.api.repository.ScheduleSubjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ScheduleSubjectServiceImpl(
    private val scheduleSubjectRepository: ScheduleSubjectRepository,
    private val classSessionService: ClassSessionService,
) : ScheduleSubjectService {
    override fun findBySubjectIdAndHourlyLoadId(
        subjectId: Long,
        hourlyLoadId: Long,
    ): List<ScheduleSubject> =
        scheduleSubjectRepository
            .findBySubjectIdAndHourlyLoadId(subjectId, hourlyLoadId)
            .initializeScheduleSessions()

    override fun getAllByIds(ids: List<Long>): List<ScheduleSubject> =
        scheduleSubjectRepository.getAllByIds(ids).initializeScheduleSessions()

    private fun List<ScheduleSubject>.initializeScheduleSessions(): List<ScheduleSubject> {
        val scheduleIds = mapTo(HashSet()) { it.schedule.id }
        if (scheduleIds.isEmpty()) return this

        val sessionsByScheduleId =
            classSessionService
                .findByScheduleIds(scheduleIds.toList())
                .groupBy { it.schedule.id }

        map { it.schedule }
            .distinctBy { it.id }
            .parallelStream()
            .forEach { schedule ->
                schedule.sessions = sessionsByScheduleId[schedule.id]
            }

        return this
    }
}
