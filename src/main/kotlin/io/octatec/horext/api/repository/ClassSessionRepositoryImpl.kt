package io.octatec.horext.api.repository

import io.octatec.horext.api.domain.ClassSession
import io.octatec.horext.api.repository.table.ClassSessionTypes
import io.octatec.horext.api.repository.table.ClassSessions
import io.octatec.horext.api.repository.table.Classrooms
import io.octatec.horext.api.repository.table.Teachers
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.anyFrom
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.select
import org.springframework.stereotype.Repository

@Repository
class ClassSessionRepositoryImpl : ClassSessionRepository {
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
            .orderBy(
                cs.scheduleId to SortOrder.ASC,
                cs.day to SortOrder.ASC,
                cs.startTime to SortOrder.ASC,
                cs.endTime to SortOrder.ASC,
                cs.id to SortOrder.ASC,
            ).map { row -> cs.createEntity(row) }
    }
}
