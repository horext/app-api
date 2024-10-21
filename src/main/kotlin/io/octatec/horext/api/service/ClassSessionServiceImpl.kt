package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import io.octatec.horext.api.repository.ClassSessionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ClassSessionServiceImpl(private val classSessionRepository: ClassSessionRepository) : ClassSessionService {

    override fun findByScheduleId(scheduleId: Long): List<ClassSession> {
        return classSessionRepository.findByScheduleId(scheduleId)
    }

    override fun findByScheduleIds(scheduleIds: List<Long>): List<ClassSession> {
        return classSessionRepository.findByScheduleIds(scheduleIds)
    }
}
