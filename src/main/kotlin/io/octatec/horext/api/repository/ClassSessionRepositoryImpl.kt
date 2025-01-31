package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.*
import org.jetbrains.exposed.sql.anyFrom
import org.jetbrains.exposed.sql.select
import org.springframework.stereotype.Repository

@Repository
class ClassSessionRepositoryImpl : ClassSessionRepository {
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
            .where { cs.scheduleId eq anyFrom(scheduleIds) }
            .map { row -> cs.createEntity(row) }
    }
}
