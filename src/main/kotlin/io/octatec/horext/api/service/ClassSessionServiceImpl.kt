package io.octatec.horext.api.service

import io.octatec.horext.api.domain.*
import org.jetbrains.exposed.sql.anyFrom
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ClassSessionServiceImpl() : ClassSessionService {

    override fun findByScheduleId(scheduleId: Long): List<ClassSession> {
        val cs = ClassSessions
        val cst = ClassSessionTypes
        val cr = Classrooms
        val t = Teachers
        return cs
            .leftJoin(cst)
            .leftJoin(cr)
            .leftJoin(t)
            .select(cs.columns + cst.columns + cr.columns + t.columns)
            .where { cs.scheduleId eq scheduleId }
            .map { row -> cs.createEntity(row) }

    }

    override fun findByScheduleIds(scheduleIds: List<Long>): List<ClassSession> {

        val cs = ClassSessions
        val cst = ClassSessionTypes
        val cr = Classrooms
        val t = Teachers
        return cs
            .leftJoin(cst)
            .leftJoin(cr)
            .leftJoin(t)
            .select(cs.columns + cst.columns + cr.columns + t.columns)
            .where { cs.scheduleId eq anyFrom(scheduleIds)  }
            .map { row -> cs.createEntity(row) }
    }
}